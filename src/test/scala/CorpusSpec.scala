import org.scalatest._
import org.apache.uima.fit.util.JCasUtil
import de.tudarmstadt.ukp.dkpro.core.api.metadata.`type`.DocumentMetaData
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.`type`.{Lemma, Token}

class CorpusSpec extends FunSpec with Matchers {
  val corpusDir =
    getClass().getClassLoader().getResource("inaugural/").getPath()

  describe("Corpus") {
    describe("readerFromDir") {
      it("should produce a CollectionReaderDescription from a directory path") {
        val reader = Corpus.readerFromDir(corpusDir)
        val params = reader.getCollectionReaderMetaData().getConfigurationParameterSettings()
        val patterns = params.getParameterValue("patterns")
        // retrieve all .txt documents
        patterns shouldBe Array("[+]**/*.txt")
      }
    }

    describe("tokenize") {
      describe("when passed a reader") {
        it("should tokenize a corpus") {
          val reader = Corpus.readerFromDir(corpusDir)
          val jcasIterator = Corpus.tokenize(reader)
          val tokenMap = (for {
            jcas <- jcasIterator
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
          val reader = Corpus.readerFromDir(corpusDir)
          val jcasIterator = Corpus.lemmatize(reader)
          val lemmaMap = (for {
            jcas <- jcasIterator
            metadata = JCasUtil.selectSingle(jcas, classOf[DocumentMetaData])
            title = metadata.getDocumentTitle()
            lemmas = JCasUtil.select(jcas, classOf[Lemma])
          } yield title -> lemmas.size()).toMap
          lemmaMap should have size 56
          all (lemmaMap.values) should be > 0
        }
      }
    }
  }
}
