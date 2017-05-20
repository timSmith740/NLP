import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
	    Annotation annotation;
	    annotation = new Annotation("Was Lincoln President? Abraham Lincoln was president.");
	    

	    // run all the selected Annotators on this text
	    pipeline.annotate(annotation);
	    int test = 0;
	    ArrayList<String> questNounsProper = new ArrayList<String>();
	    ArrayList<String> questNouns = new ArrayList<String>();
	    ArrayList<String> questVerbs = new ArrayList<String>();
	    ArrayList<String> ansNounsProper = new ArrayList<String>();
	    ArrayList<String> ansNouns = new ArrayList<String>();
	    ArrayList<String> ansVerbs = new ArrayList<String>();
	    String question;
	    String answer;
	    
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    for (CoreMap sentence: sentences){
	    	if (test == 0){
	    		question = sentence.toString();
	    	}
	    	if (test == 1){
	    		answer = sentence.toString();
	    	}
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)){
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		String word = token.get(TextAnnotation.class);
	    		System.out.println(pos);
	    		if (test == 0){
	    			if (pos.equals("NNP")){
	    				questNounsProper.add(word);
	    			} else if (pos.equals("NN")){
	    				questNouns.add(word);
	    			} else if (pos.equals("VBD")){
	    				System.out.println("Test");
	    				questVerbs.add(word);
	    			} else if (pos.equals("VB")){
	    				questVerbs.add(word);
	    			}
	    		}
	    		if (test == 1){
	    			if (pos.equals("NNP")){
	    				ansNounsProper.add(word);
	    			} else if (pos.equals("NN")){
	    				ansNouns.add(word);
	    			} else if (pos.equals("VBD")){
	    				System.out.println("Test");
	    				ansVerbs.add(word);
	    			} else if (pos.equals("VB")){
	    				ansVerbs.add(word);
	    			}
	    		}
	    	}
	    	test++;
	    }
	    int score = 0;
	    for (String current : questNounsProper){
	    	System.out.println(current);
	    	if (ansNounsProper.contains(current)){
	    		score++;
	    	}
	    }
	    int nounsProper = questNounsProper.size() / score;
	    score = 0;
	    for (String current : questNouns){
	    	if (ansNounsProper.contains(current)){
	    		score++;
	    	}
	    }
	    int nouns = 0;
	    if (score != 0){
	    	nouns = questNouns.size() / score;
	    }
	    score = 0;
	    for (String current : questVerbs){
	    	if (ansVerbs.contains(current)){
	    		score++;
	    	}
	    }
	    System.out.println(score);
	    int verbs = 0;
	    if (score != 0){
	    	verbs = questVerbs.size() / score;
	    }
	    System.out.println(nounsProper);
	    System.out.println(nouns);
	    System.out.println(verbs);
	    	/*SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
    		System.out.println(dependencies.toList());
    		System.out.println(dependencies.toPOSList());
    		System.out.println(dependencies.toString());*/
	    	/*Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		    out.println("The first sentence parse tree is:");
		    tree.pennPrint(out);
		    System.out.println(tree.getChildrenAsList());
		    
		    Tree[] children = tree.children();
		    System.out.println(tree.pennString().split(" ").toString());
		    for (Tree child: children){
		    	System.out.println("test");
		    	System.out.println(child.flatten());
		    	//if (child.value().equals("S")){
		    		List<Tree> leaves = child.getLeaves();
		    		for (Tree leaf : leaves){
		    			leaf.headTerminal(hf, parent)
		    			//if(leaf.value().equals("NP")){
		    			List<Word> words = leaf.yieldWords();
		    			for (Word word: words){
		    				System.out.println(String.format("(%s - NP),",word.word()));
		    			}
		    			//}
		    		}
		    	//}
		    }*/
	    
	    
	    /*Map<Integer, CorefChain> graph = annotation.get(CorefChainAnnotation.class);
	    System.out.println(graph.keySet());*/
		
		/*Annotation document = new Annotation("Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.");
	    Properties props = new Properties();
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,coref");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    pipeline.annotate(document);
	    System.out.println("---");
	    System.out.println("coref chains");
	    for (edu.stanford.nlp.coref.data.CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
	      System.out.println("\t" + cc);
	    }
	    for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
	      System.out.println("---");
	      System.out.println("mentions");
	      for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
	        System.out.println("\t" + m);
	       }
	    }*/
		
		/*Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
	    Annotation annotation;
	    
	    annotation = new Annotation("Who was president during the Civil War.");


	    // run all the selected Annotators on this text
	    pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);*/
	    /*for (CoreMap sentence: sentences){
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)){
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		String word = token.get(TextAnnotation.class);
	    		System.out.println(token.toString());
	    		System.out.println(pos);
	    		System.out.println(word);
	    	}
	    	SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
    		System.out.println(dependencies.toList());
    		System.out.println(dependencies.toPOSList());
    		System.out.println(dependencies.toString());
	    }*/
		/*CoreMap sentence = sentences.get(0);
		Tree parse = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		List<String> result = new ArrayList<>();
	    TregexPattern pattern = TregexPattern.compile("@NP");
	    TregexMatcher matcher = pattern.matcher(parse);
	    while (matcher.find()) {
	        Tree match = matcher.getMatch();
	        List<Tree> leaves = match.getLeaves();
	        System.out.println(leaves);
	        // Some Guava magic.
	        String nounPhrase = Joiner.on(' ').join(Lists.transform(leaves, Functions.toStringFunction()));
	        result.add(nounPhrase);
	        List<LabeledWord> labeledYield = match.labeledYield();
	        System.out.println("labeledYield: " + labeledYield);
	    }*/
	    
	}
	
	/*public static void dfs(Tree node, Tree parent, HeadFinder headFinder){
		
	}*/

}
