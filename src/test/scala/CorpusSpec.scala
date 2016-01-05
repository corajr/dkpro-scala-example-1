import org.scalatest._
import org.apache.uima.fit.util.JCasUtil
import de.tudarmstadt.ukp.dkpro.core.api.metadata.`type`.DocumentMetaData
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.`type`.{Lemma, Token}

class CorpusSpec extends FunSpec with Matchers {
  val corpusDir =
    getClass().getClassLoader().getResource("inaugural/").getPath()

  describe("Corpus") {
    describe("fromDir") {
      it("should produce a Corpus with a CollectionReaderDescription from a directory path") {
        val corpus = Corpus.fromDir(corpusDir)
        val params = corpus.reader.getCollectionReaderMetaData().getConfigurationParameterSettings()
        val patterns = params.getParameterValue("patterns")
        // retrieve all .txt documents
        patterns shouldBe Array("[+]**/*.txt")
      }
    }

    describe("tokenize") {
      describe("when passed a reader") {
        it("should tokenize a corpus") {
          val corpus = Corpus.fromDir(corpusDir)
          val tokenMap = (for {
            jcas <- corpus.tokenize()
            metadata = JCasUtil.selectSingle(jcas, classOf[DocumentMetaData])
            title = metadata.getDocumentTitle()
            tokens = JCasUtil.select(jcas, classOf[Token])
          } yield title -> tokens.size()).toMap
          tokenMap should have size 56
          all (tokenMap.values) should be > 0
        }        
      }
    }

    describe("lemmatize") {
      describe("when passed a reader") {
        it("should lemmatize a corpus") {
          val corpus = Corpus.fromDir(corpusDir)
          val lemmaMap = (for {
            jcas <- corpus.lemmatize()
            metadata = JCasUtil.selectSingle(jcas, classOf[DocumentMetaData])
            title = metadata.getDocumentTitle()
            lemmas = JCasUtil.select(jcas, classOf[Lemma])
          } yield title -> lemmas.size()).toMap
          lemmaMap should have size 56
          all (lemmaMap.values) should be > 0
        }
        it("should return the expected lemmas from part of the first document") {
          import scala.collection.JavaConversions._
          
          val corpus = Corpus.fromDir(corpusDir)
          val jcasIterator = corpus.lemmatize()
          val jcas = jcasIterator.next()
          val lemmas = JCasUtil.select(jcas, classOf[Lemma]).take(5).map(_.getValue)
          lemmas shouldBe List("fellow", "-", "citizen", "of", "the")
        }
      }
    }
  }
}
