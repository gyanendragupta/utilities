import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConnectionLeakCheck {
	
	public static void main(String[] a) {
		try (Stream<Path> paths = Files.walk(Paths.get("C://Users//Gyanendra Gupta"))) {
			paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(new checkConnectionLeak());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class checkConnectionLeak implements Consumer<Path> {
	@Override
	public void accept(Path path) {
		String line;
		int getConnectionCounter = 0;
		int closeConnectionCounter = 0;
		FileReader fr = null;
		BufferedReader br = null;
		try {			
			fr = new FileReader(path.toString());
			br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				if (!line.trim().startsWith("//")){
					if (line.contains("openSession()")){
						getConnectionCounter++;
					} else if (line.contains("ession.close()")){
						closeConnectionCounter++;
					}
				}				
			}
			if (getConnectionCounter > closeConnectionCounter){
				System.out.println(path + " getConnectionCounter: "+ getConnectionCounter + " AND closeConnectionCounter: " +closeConnectionCounter);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br!=null){
					br.close();
				}
				if (fr!=null){
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
}
