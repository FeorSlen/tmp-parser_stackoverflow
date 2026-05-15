error id: file://<HOME>/work/%D0%BC%D0%BE%D1%91/%D1%81%D1%82%D0%B0%D0%B6%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0/1%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%BC%D0%B5%D0%BD%D0%B5%D0%B4%D0%B6%D0%B5%D1%80%20%D0%BF%D0%B0%D1%80%D0%BE%D0%BB%D0%B5%D0%B9/stackoverflow_parser/src/main/scala/Models.scala:_empty_/Item#
file://<HOME>/work/%D0%BC%D0%BE%D1%91/%D1%81%D1%82%D0%B0%D0%B6%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0/1%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%BC%D0%B5%D0%BD%D0%B5%D0%B4%D0%B6%D0%B5%D1%80%20%D0%BF%D0%B0%D1%80%D0%BE%D0%BB%D0%B5%D0%B9/stackoverflow_parser/src/main/scala/Models.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol _empty_/Item#
empty definition using fallback
non-local guesses:

offset: 89
uri: file://<HOME>/work/%D0%BC%D0%BE%D1%91/%D1%81%D1%82%D0%B0%D0%B6%D0%B8%D1%80%D0%BE%D0%B2%D0%BA%D0%B0/1%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5%20%D0%BC%D0%B5%D0%BD%D0%B5%D0%B4%D0%B6%D0%B5%D1%80%20%D0%BF%D0%B0%D1%80%D0%BE%D0%BB%D0%B5%D0%B9/stackoverflow_parser/src/main/scala/Models.scala
text:
```scala
import zio.json.*

case class Owner(display_name: String) derives JsonCodec

case class I@@tem(
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

```


#### Short summary: 

empty definition using pc, found symbol in pc: 