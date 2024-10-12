package telran.persistence;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
record SourceCodeComments(Path source, String code, String comments) {}
public class CodeCommentsSeparation {
    public static void main(String[] args) {
        //TODO - data from args[0] split to two files: args[1], args[2]
        //for sake of simplicity comments may be only on one line, like comments at this file
        // /* */ cannot be
        // code ...// comment .... cannot be
             //However // may be not only at beginning of line, like this
        //args[0] - path to file containing code and comments 
        //args[1] - path to file for placing only code
        //args[2] - path to file for placing only comments
        try {
			SourceCodeComments scc = processArguments (args);
			codeCommentsSeparation(scc);
		} catch (RuntimeException e) {
			
			e.printStackTrace();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	private static void codeCommentsSeparation(SourceCodeComments scc) throws Exception {
		try(BufferedReader reader = Files.newBufferedReader(scc.source());
				PrintWriter commentsWriter = new PrintWriter(scc.comments());
				PrintWriter codeWriter = new PrintWriter(scc.code())) {
			reader.lines().forEach(l -> (l.trim().startsWith("//") ?
					commentsWriter : codeWriter).println(l));
		}
		
	}

	private static SourceCodeComments processArguments (String[] args)throws Exception {
		if(args.length != 3) {
			throw new Exception("too few arguments");
		}
		Path sourcePath = Path.of(args[0]);
		
		if (!Files.exists(sourcePath)) {
			throw new Exception(String.format("%s doesn't exist",
					sourcePath.toAbsolutePath().normalize()));
		}
		
		return new SourceCodeComments(sourcePath, args[1], args[2]);
	}
    }

