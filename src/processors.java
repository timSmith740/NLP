import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class processors {

	public static void main(String[] args) throws FileNotFoundException {
		
		/*Document doc = new Document("Lincoln was president. He was a boy.");
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
        }*/
		
		PrintWriter out;
	    if (args.length > 1) {
	      out = new PrintWriter(args[1]);
	    } else {
	      out = new PrintWriter(System.out);
	    }
	    PrintWriter xmlOut = null;
	    if (args.length > 2) {
	      xmlOut = new PrintWriter(args[2]);
	    }
		
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, parse");

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
	    Annotation annotation;
	    if (args.length > 0) {
	      annotation = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
	    } else {
	      annotation = new Annotation("Who was president during the Civil War.");
	    }

	    // run all the selected Annotators on this text
	    pipeline.annotate(annotation);
	    
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    for (CoreMap sentence: sentences){
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)){
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		String word = token.get(TextAnnotation.class);
	    		System.out.println(token.toString());
	    		System.out.println(pos);
	    		System.out.println(word);
	    	}
	    	SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
    		System.out.println(dependencies.toList());
	    }
	    
	    /*CoreMap sentence = sentences.get(0);
	    
	    Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
	    out.println("The first sentence parse tree is:");
	    tree.pennPrint(out);
	    System.out.println(tree.getChildrenAsList());
	    System.out.println(tree.toString().split("))").length);
	    
	    out.println();*/
	    
	    
	    
	}

}
