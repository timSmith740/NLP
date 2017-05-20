import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class processors {

	public static void main(String[] args) {
		// TODO Auto-generated method stub.
		Document doc = new Document("Lincoln was president. He was a boy.");
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            // We're only asking for words -- no need to load any models yet
            System.out.println("The second word of the sentence '" + sent + "' is " + sent.word(1));
            // When we ask for the lemma, it will load and run the part of speech tagger
            System.out.println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2));
            // When we ask for the parse, it will load and run the parser
            System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
            System.out.println(sent.nerTags());
            System.out.println(sent.posTags());
            System.out.println(sent.words());
            System.out.println(sent.lemmas());
            System.out.println(sent.governor(1));
            System.out.println(sent.natlogPolarities());
        }
	}

}
