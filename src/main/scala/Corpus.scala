import org.apache.uima.collection.CollectionReaderDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription
import org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription
import org.apache.uima.fit.pipeline.SimplePipeline.iteratePipeline
import org.apache.uima.jcas.JCas
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader
import de.tudarmstadt.ukp.dkpro.core.clearnlp.{ClearNlpSegmenter, ClearNlpLemmatizer, ClearNlpPosTagger}
import scala.collection.JavaConversions._

object Corpus {
  val lang = "en"

  def readerFromDir(directory: String): CollectionReaderDescription =
    createReaderDescription(
      classOf[TextReader],
      ResourceCollectionReaderBase.PARAM_SOURCE_LOCATION, directory,
      ResourceCollectionReaderBase.PARAM_PATTERNS, "[+]**/*.txt",
      ResourceCollectionReaderBase.PARAM_LANGUAGE, lang)

  def tokenize(reader: CollectionReaderDescription): Iterator[JCas] =
    iteratePipeline(
      reader,
      createEngineDescription(classOf[ClearNlpSegmenter])
    ).iterator()

  def lemmatize(reader: CollectionReaderDescription): Iterator[JCas] =
    iteratePipeline(
      reader,
      createEngineDescription(classOf[ClearNlpSegmenter]),
      createEngineDescription(classOf[ClearNlpPosTagger]),
      createEngineDescription(classOf[ClearNlpLemmatizer])
    ).iterator()
}
