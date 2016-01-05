import org.apache.uima.collection.CollectionReaderDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import org.apache.uima.fit.pipeline.SimplePipeline.iteratePipeline
import org.apache.uima.jcas.JCas
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader
import de.tudarmstadt.ukp.dkpro.core.clearnlp.{ClearNlpSegmenter, ClearNlpLemmatizer, ClearNlpPosTagger}

case class Corpus(reader: CollectionReaderDescription) {
  // keep implicit conversions local
  import scala.collection.JavaConversions._

  def tokenize(): Iterator[JCas] =
    iteratePipeline(
      reader,
      createEngineDescription(classOf[ClearNlpSegmenter])
    ).iterator()

  def lemmatize(): Iterator[JCas] =
    iteratePipeline(
      reader,
      createEngineDescription(classOf[ClearNlpSegmenter]),
      createEngineDescription(classOf[ClearNlpPosTagger]),
      createEngineDescription(classOf[ClearNlpLemmatizer])
    ).iterator()
}

object Corpus {
  def fromDir(directory: String, pattern: String = "[+]**/*.txt", lang: String = "en"): Corpus =
    Corpus(createReaderDescription(
      classOf[TextReader],
      ResourceCollectionReaderBase.PARAM_SOURCE_LOCATION, directory,
      ResourceCollectionReaderBase.PARAM_PATTERNS, pattern,
      ResourceCollectionReaderBase.PARAM_LANGUAGE, lang))
}
