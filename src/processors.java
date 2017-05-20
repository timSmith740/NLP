import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class processors {

	public static final float SCORE_TRUTH_THRESHOLD = 0.8f;
	public static final String FILE_NAME = "lincoln.txt";
	
	public static final String[] NEGATING_WORDS = {"isn't", "wasn't", "not"};
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanner = new Scanner(System.in);
		
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    while(true){
	    	System.out.println("Enter a query");
		    String query = scanner.nextLine();
		    if (query.equals("q")){
		    	break;
		    }
		    int linenum = 0;
		    
		    Annotation queryAnnotation = new Annotation(query);
		    
		    ArrayList<String> questNouns = new ArrayList<>();
		    ArrayList<String> questVerbs = new ArrayList<>();
		    int nounAndVerbCount = 0;
		    
		    pipeline.annotate(queryAnnotation);
		    CoreMap queryCore = queryAnnotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
	    	for (CoreLabel token: queryCore.get(TokensAnnotation.class)){
	    		String pos = token.get(PartOfSpeechAnnotation.class);
	    		String word = token.get(TextAnnotation.class);
	    		System.out.println(pos);
				if (pos.startsWith("N")){
					questNouns.add(word.toLowerCase());
				}else if (pos.startsWith("V")){
					questVerbs.add(word.toLowerCase());
				}
	    	}
	    	nounAndVerbCount = questNouns.size() + questVerbs.size();
		    
	    	fileLoop: for(String line: IOUtils.linesFromFile(FILE_NAME)) {
		    	
			    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
			    Annotation annotation;
			    annotation = new Annotation(line);
		
			    // run all the selected Annotators on this text
			    pipeline.annotate(annotation);
			    
			    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			    for (CoreMap sentence: sentences){
			    	int sentencenum = 0;
				    ArrayList<String> ansNouns = new ArrayList<>();
				    ArrayList<String> ansVerbs = new ArrayList<>();
			    	
			    	for (CoreLabel token: sentence.get(TokensAnnotation.class)){
			    		String pos = token.get(PartOfSpeechAnnotation.class);
			    		String word = token.get(TextAnnotation.class);
			    		
		    			if (pos.startsWith("N")){
		    				ansNouns.add(word.toLowerCase());
		    			}else if (pos.startsWith("V")){
		    				ansVerbs.add(word.toLowerCase());
		    			}
			    	}
			    	
				    int score = 0;
				    for (String current : questNouns){
				    	if (ansNouns.contains(current)){
				    		score++;
				    	}
				    }
				    int nouns = 0;
				    if (score != 0){
				    	nouns = score;
				    }
				    score = 0;
				    for (String current : questVerbs){
				    	if (ansVerbs.contains(current)){
				    		score++;
				    	}
				    }
				    int verbs = 0;
				    if (score != 0){
				    	verbs = score;
				    }
				    int totalScore = nouns + verbs;
				    
				    float accuracy = totalScore * 1.0f / nounAndVerbCount;
				    
			    	if(accuracy > 0.25f) {
			    		System.out.println("Accuracy for line " + linenum + ", sentence " + sentencenum + ": " + accuracy);
			    	}
				    
				    if (accuracy > SCORE_TRUTH_THRESHOLD){
				    	System.out.println("Found a match");
				    	break fileLoop;
				    }
				    
				    sentencenum++;
			    }
			    
		    	linenum++;
		    }
	    }
	    scanner.close();
	    
	}
}
