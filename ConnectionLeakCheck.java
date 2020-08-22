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
		try (Stream<Path> paths = Files.walk(Paths.get("C://Users//Gyanendra Gupta//git"))) {
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
		FileReader fr;
		try {			
			fr = new FileReader(path.toString());
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				if (line.contains("openSession()")){
					getConnectionCounter++;
				} else if (line.contains("ession.close()")){
					closeConnectionCounter++;
				}
			}
			if (getConnectionCounter > closeConnectionCounter){
				System.out.println(path + " getConnectionCounter: "+ getConnectionCounter + " AND closeConnectionCounter: " +closeConnectionCounter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
			fr.close();
		}
	    
		//path.getName(-1);
		//System.out.println(path);
		
	}
	
}
