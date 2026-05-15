import zio.json.*

case class Owner(display_name: String) derives JsonCodec

case class Item(
  question_id:  Long,
  title:        String,
  link:         String,
  tags:         List[String],
  score:        Int,
  answer_count: Int,
  view_count:   Int,
  is_answered:  Boolean,
  owner:        Owner,
) derives JsonCodec

case class SearchResponse(
  items:           List[Item],
  has_more:        Boolean,
  quota_remaining: Int,
  quota_max:       Int,
  backoff:         Option[Int],
) derives JsonCodec
