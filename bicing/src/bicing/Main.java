package bicing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;

import IA.Bicing.Estaciones;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
	
	public static void main(String[] args) {
		// args[0] nombre del fichero de entrada
		//Path path = Paths.get(System.getProperty("user.dir"), args[0]);
		Path path = Paths.get(System.getProperty("user.dir"), "file/exemple.txt");

		Charset charset = Charset.forName("ISO-8859-1");
		try {
			List<String> lines = Files.readAllLines(path, charset);
			String alg = getString(lines.get(10));
			long startTime = System.currentTimeMillis();
			if (alg.equals("HC")) HC(lines);
			else SA(lines);
			long endTime = System.currentTimeMillis();
			System.out.println("Ha tardado " + (endTime - startTime) + " milliseconds");
		    } catch (Exception e) {
		      System.out.println(e);
		    }		
	}
	
	private static int readDemanda(String linia) {
		int dem = Estaciones.RUSH_HOUR;
		String demS = getString(linia); //AIXO NO HO TINC CLAR, HAURE DE PARLAR AMB LA PATRI
		if (demS.equals("EQUILIBRIUM")) dem = Estaciones.EQUILIBRIUM;
		return dem;
	}
	
	private static void HC(List<String> lines) throws Exception{
		System.out.println("HillClimbing\n");
		//Leemos datos necesarios para HC
		int numB = getNum(lines.get(4));
		int numE = getNum(lines.get(5));
		int numF = getNum(lines.get(6));
		int dem = readDemanda(lines.get(7));
		int seed = getNum(lines.get(8));
		GeneraProblema.CrearProblema(numE, numB, dem, seed);
		System.out.println("Pepe");
		Estado estado = Estado.estadoInicial(numF, numE);
		estado.print();
		System.out.println("maria");
		SuccessorsHC succ = new SuccessorsHC();
		System.out.println("juan");
		EstadoFinal ef = new EstadoFinal();
		System.out.println("Octavi");
		int numh = getNum(lines.get(23));
		Problem problem = null;
		switch (numh) {
			case 1: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica1()); 
				break;
			case 2: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica2()); 
				break;
			default: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica3()); 
				break;			
		}
		System.out.println("Pepe3");
		Search search = new HillClimbingSearch();
		System.out.println("Pepe4");
		SearchAgent agent = new SearchAgent(problem, search);
		System.out.println("Pepe5");
		estado.print();
		Estado result = (Estado) search.getGoalState();
		result.print();
		if (getString(lines.get(13)).equals("S")) printActions(agent.getActions());
		System.out.println("Pepe6");
		if (getString(lines.get(14)).equals("S")) printInstrumentation(agent.getInstrumentation());
	}
	
	private static void SA(List<String> lines) throws Exception {
		System.out.println("Simulated Annealing\n");
		//Leemos datos necesarios para SA
		int numB = getNum(lines.get(4));
		int numE = getNum(lines.get(5));
		int numF = getNum(lines.get(6));
		int dem = readDemanda(lines.get(7));
		int seed = getNum(lines.get(8));
		int itMAX = getNum(lines.get(17));
		int it = getNum(lines.get(18));
		int k = getNum(lines.get(19));
		double lam = getDouble(lines.get(20));
		
		GeneraProblema.CrearProblema(numE, numB, dem, seed);
		Estado estado = Estado.estadoInicial(numF, numE);
		SuccessorsSA succ = new SuccessorsSA();
		EstadoFinal ef = new EstadoFinal();
		int numh = getNum(lines.get(23));
		Problem problem = null;
		switch (numh) {
			case 1: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica1()); 
				break;
			case 2: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica2()); 
				break;
			default: 
				problem = new Problem(estado, succ, ef, new FuncionHeuristica3()); 
				break;			
		}
		Search search = new SimulatedAnnealingSearch(itMAX, it, k, lam);
		SearchAgent agent = new SearchAgent(problem, search);
		if (getString(lines.get(13)) == "S") printActions(agent.getActions());
		if (getString(lines.get(14)) == "S") printInstrumentation(agent.getInstrumentation());
	}
	
	//Funcions auxiliars
	
	private static int getNum(String s) {
		int i = s.indexOf(':');
		String num = s.substring(i+2);
		return Integer.parseInt(num);
	}
	
	private static String getString(String s) {
		int i = s.indexOf(':');
		return s.substring(i+2);
	}
	
	private static double getDouble(String s) {
		int i = s.indexOf(':');
		String num = s.substring(i+2);
		return Double.parseDouble(num);
	}
	
	private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }
    
    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}
