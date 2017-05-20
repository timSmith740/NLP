import java.io.FileNotFoundException;
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
		
		Properties props = new Properties();
	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.
	    Annotation annotation;
	    annotation = new Annotation("Was Lincoln president? Abraham Lincoln was president.");
	    

	    // run all the selected Annotators on this text
	    pipeline.annotate(annotation);
	    int test = 0;
	    ArrayList<String> questNounsProper = new ArrayList<>();
	    ArrayList<String> questNouns = new ArrayList<>();
	    ArrayList<String> questVerbs = new ArrayList<>();
	    ArrayList<String> ansNounsProper = new ArrayList<>();
	    ArrayList<String> ansNouns = new ArrayList<>();
	    ArrayList<String> ansVerbs = new ArrayList<>();
	    String question= "";
	    String answer ="";
	    
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
	    		if (test == 0){
	    			if (pos.equals("NNP")){
	    				questNounsProper.add(word.toLowerCase());
	    			} else if (pos.equals("NN")){
	    				questNouns.add(word.toLowerCase());
	    			} else if (pos.equals("VBD")){
	    				questVerbs.add(word.toLowerCase());
	    			} else if (pos.equals("VB")){
	    				questVerbs.add(word.toLowerCase());
	    			}
	    		}
	    		if (test == 1){
	    			if (pos.equals("NNP")){
	    				ansNounsProper.add(word.toLowerCase());
	    			} else if (pos.equals("NN")){
	    				ansNouns.add(word.toLowerCase());
	    			} else if (pos.equals("VBD")){
	    				ansVerbs.add(word.toLowerCase());
	    			} else if (pos.equals("VB")){
	    				ansVerbs.add(word.toLowerCase());
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
	    int nounsProper = 0;
	    if (score != 0){
	    	nounsProper = score;
	    }
	    score = 0;
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
	    int totalScore = nounsProper + nouns + verbs;
	    if (totalScore / question.split(" ").length  > 0.8){
	    	System.out.println("Found a match");
	    }
	    	
	}
}
