import zio.*

trait ParserOrchestrator:
  def run(tags: List[String]): Task[Unit]

object ParserOrchestratorLive:
  val layer: URLayer[AppConfig & StackOverflowClient & ResultWriter, ParserOrchestrator] =
    ZLayer.fromFunction(ParserOrchestratorLive(_, _, _))

case class ParserOrchestratorLive(
  config: AppConfig,
  client: StackOverflowClient,
  writer: ResultWriter,
) extends ParserOrchestrator:

  def run(tags: List[String]): Task[Unit] =
    Semaphore.make(config.maxParallel).flatMap { sem =>
      ZIO.foreachParDiscard(tags)(tag => sem.withPermit(processTag(tag)))
    }

  private def processTag(tag: String): Task[Unit] =
    ZIO.foreachParDiscard(1 to config.pages) { page =>
      (for
        resp <- client.search(tag, page)
        _    <- writer.write(tag, page, resp)
        _    <- resp.backoff match
                  case Some(wait) => ZIO.succeed(println(s"[WAIT] backoff ${wait}s for $tag")) *> ZIO.sleep(wait.seconds)
                  case None       => ZIO.unit
      yield ())
        .catchAll {
          case SoNotFound(t, p)  => ZIO.succeed(println(s"[SKIP] not found: $t page $p"))
          case SoForbidden(t, p) => ZIO.succeed(println(s"[ERROR] forbidden: $t page $p — bad key or quota")) *> ZIO.fail(SoForbidden(t, p))
          case e                 => ZIO.fail(e)
        }
    }
