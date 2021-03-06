package bicing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import IA.Bicing.Estaciones;
import aima.datastructures.PriorityQueue;
import IA.Bicing.Estacion;

/**
 * Proyecto de IA- Busqueda local
 * 
 * Clase que define el estado del problema.
 * 
 * 
 * @author Patricia
 *
 */
public class Estado {

	private Furgoneta[] vFurgonetas;
	private Struct[] vEstaciones;
   
	private Double costeGasolina;
	
	private Integer BicisBienColocadas;
	private Integer BicisMalColocadas;
	

	
	public Estado(Furgoneta[] vFurgonetas, Struct[] vEstaciones, Double costeGasolina, Integer bicisBenColocades,
			Integer bicisMalColocades) {
		this.vFurgonetas = vFurgonetas;
		this.vEstaciones = vEstaciones;
		this.costeGasolina = costeGasolina;
		BicisBienColocadas = bicisBenColocades;
		BicisMalColocadas = bicisMalColocades;
	}


	public Estado(){
		vFurgonetas=null;
		vEstaciones=null;
		costeGasolina=0.0;
		BicisBienColocadas=0;
		BicisMalColocadas=0;
		
	}

	private Furgoneta[] copyvFurg(Furgoneta[] vFurg){
		Furgoneta[] newe = new Furgoneta[vFurg.length];
		for(int i=0; i<vFurg.length;++i){
			newe[i]= new Furgoneta(vFurg[i]);
		}
		return newe;
	}
	
	

	public Integer getBicisBienColocadas() {
		return BicisBienColocadas;
	}

	public void setBicisBienColocadas(Integer bicisBienColocades) {
		BicisBienColocadas = bicisBienColocades;
	}

	public Integer getBicisMalColocadas() {
		return BicisMalColocadas;
	}

	public void setBicisMalColocadas(Integer bicisMalColocades) {
		BicisMalColocadas = bicisMalColocades;
	}

	
	private Struct[] copyvStruct(Struct[] vSt){
		Struct[] newe = new Struct[vSt.length];
		for(int i=0; i<vSt.length;++i){
			newe[i]= new Struct(vSt[i]);
		}
		return newe;
	}
	
	public Estado(Estado E1){
		vFurgonetas=copyvFurg(E1.vFurgonetas);
		vEstaciones=copyvStruct(E1.vEstaciones);
		costeGasolina=E1.costeGasolina;	
		BicisBienColocadas=E1.BicisBienColocadas;
		BicisMalColocadas=E1.BicisMalColocadas;
	}
	public Furgoneta[] getvFurgonetas() {
		return vFurgonetas;
	}
	public void setvFurgonetas(Furgoneta[] vFurgonetas) {
		this.vFurgonetas = vFurgonetas;
	}
	public Struct[] getvEstaciones() {
		return vEstaciones;
	}
	public void setvEstaciones(Struct[] vEstaciones) {
		this.vEstaciones = vEstaciones;
	}
	
	public Double getCosteGasolina() {
		return costeGasolina;
	}
	public void setCosteGasolina(Double costeGasolina) {
		this.costeGasolina = costeGasolina;
	}
	
	
	private Integer bicisBienColocadasIndexE(Integer e){
		if(e==-1)return 0;
		Integer falten=GeneraProblema.getEstacion(e).getDemanda()-GeneraProblema.getEstacion(e).getNumBicicletasNext();
		Integer coloquem = vEstaciones[e].getBicisColocades()-vEstaciones[e].getBicisAgafen();
		if(falten>0 && coloquem>0)return minim(falten,coloquem);
		return 0;
		
	}
	
	
	private Integer bicisMalColocadasIndexE(Integer e){
		if(e==-1)return 0;
		Estacion Es=GeneraProblema.getEstacion(e);
		Integer mal=0;
		Integer hay=Es.getNumBicicletasNext();
		Integer colocamos=vEstaciones[e].getBicisColocades();
		Integer queremos= Es.getDemanda();
		Integer quitamos=vEstaciones[e].getBicisAgafen(); 
		if(quitamos>colocamos){
			quitamos=quitamos-colocamos;
			Integer sobran=hay-queremos;
			if(sobran > 0){
				if(sobran < quitamos){
					mal=quitamos-sobran;
				}
			}
			else{
				mal=quitamos;
			}
			
		}
		
		
		return mal;
	}
	
	private static Integer bicisBienColocadasIndexE(Integer e,Struct[] vEst){
		if(e==-1)return 0;
		Integer falten=GeneraProblema.getEstacion(e).getDemanda()-GeneraProblema.getEstacion(e).getNumBicicletasNext();
		Integer coloquem = vEst[e].getBicisColocades()-vEst[e].getBicisAgafen();
		if(falten>0 && coloquem>0)return minim(falten,coloquem);
		return 0;
		
	}
	
	
	private static Integer bicisMalColocadasIndexE(Integer e, Struct[] vEst){
		if(e==-1)return 0;
		Estacion Es=GeneraProblema.getEstacion(e);
		Integer mal=0;
		Integer hay=Es.getNumBicicletasNext();
		Integer colocamos=vEst[e].getBicisColocades();
		Integer queremos= Es.getDemanda();
		Integer quitamos=vEst[e].getBicisAgafen(); 
		if(quitamos>colocamos){
			quitamos=quitamos-colocamos;
			Integer sobran=hay-queremos;
			if(sobran > 0 ){
				if(sobran < quitamos){
					mal=quitamos-sobran;
				}
			}
			else{
				mal=quitamos;
			}
			
		}
		
		
		return mal;
		
	}
	
	
	public void incrementarNNENP1(Integer n, Integer numFurgoneta){
		//////System.out.println(" incrementarNNENP1");
		
		Integer iP=vFurgonetas[numFurgoneta].getindexEstacioP1();
		Integer iE=vFurgonetas[numFurgoneta].getindexEstacioE();
		
		Integer bienE=bicisBienColocadasIndexE(iE);
		Integer bienP1=bicisBienColocadasIndexE(iP);
		Integer malE=bicisMalColocadasIndexE(iE);
		Integer malP1=bicisMalColocadasIndexE(iP);
		
		vFurgonetas[numFurgoneta].setNp1(vFurgonetas[numFurgoneta].getNp1()+n);
		
		if(iP != -1)vEstaciones[iP].sumaNBicis(n);
		if(iE != -1)vEstaciones[iE].sumaNBicisAg(n);
		
		Integer bienEv2=bicisBienColocadasIndexE(iE);
		Integer bienP1v2=bicisBienColocadasIndexE(iP);
		Integer malEv2=bicisMalColocadasIndexE(iE);
		Integer malP1v2=bicisMalColocadasIndexE(iP);
		
		
		BicisBienColocadas= BicisBienColocadas-bienP1+bienP1v2-bienE+bienEv2;
		BicisMalColocadas= BicisMalColocadas-malE+malEv2-malP1+malP1v2;
		
	}
	
	public void incrementarNNP1decrementarNP2(Integer n, Integer numFurgoneta){
		
		//////System.out.println(" incrementarNNP1decrementarNP2");
		
		Integer P1=vFurgonetas[numFurgoneta].getindexEstacioP1();
		Integer P2=vFurgonetas[numFurgoneta].getindexEstacioP2();
		
		Integer bienP1=bicisBienColocadasIndexE(P1);
		Integer bienP2=bicisBienColocadasIndexE(P2);
		Integer malP1=bicisMalColocadasIndexE(P1);
		Integer malP2=bicisMalColocadasIndexE(P2);
		
		vFurgonetas[numFurgoneta].setNp2(vFurgonetas[numFurgoneta].getNp2()-n);
		vFurgonetas[numFurgoneta].setNp1(vFurgonetas[numFurgoneta].getNp1()+n);
		if(P1!=-1)vEstaciones[P1].sumaNBicis(n);
		if(P2!=-1)vEstaciones[P2].restaNBicis(n);
		
		Integer bienP1v2=bicisBienColocadasIndexE(P1);
		Integer bienP2v2=bicisBienColocadasIndexE(P2);
		Integer malP1v2=bicisMalColocadasIndexE(P1);
		Integer malP2v2=bicisMalColocadasIndexE(P2);
		
		BicisBienColocadas= BicisBienColocadas-bienP1-bienP2+bienP1v2+bienP2v2;
		BicisMalColocadas= BicisMalColocadas-malP1-malP2+malP1v2+malP2v2;
		
	}
	
	public void incrementarNNP2decrementarNP1(Integer n, Integer numFurgoneta){
		//////System.out.println(" incrementarNNP2decrementarNP1");
		
		Integer P1=vFurgonetas[numFurgoneta].getindexEstacioP1();
		Integer P2=vFurgonetas[numFurgoneta].getindexEstacioP2();

		Integer bienP1=bicisBienColocadasIndexE(P1);
		Integer bienP2=bicisBienColocadasIndexE(P2);
		Integer malP1=bicisMalColocadasIndexE(P1);
		Integer malP2=bicisMalColocadasIndexE(P2);
		
		vFurgonetas[numFurgoneta].setNp2(vFurgonetas[numFurgoneta].getNp2()+n);
		vFurgonetas[numFurgoneta].setNp1(vFurgonetas[numFurgoneta].getNp1()-n);
		if(P2 != -1)vEstaciones[P2].sumaNBicis(n);
		if(P1 != -1)vEstaciones[P1].restaNBicis(n);
		
		Integer bienP1v2=bicisBienColocadasIndexE(P1);
		Integer bienP2v2=bicisBienColocadasIndexE(P2);
		Integer malP1v2=bicisMalColocadasIndexE(P1);
		Integer malP2v2=bicisMalColocadasIndexE(P2);
		
		BicisBienColocadas= BicisBienColocadas-bienP1-bienP2+bienP1v2+bienP2v2;
		BicisMalColocadas= BicisMalColocadas-malP1-malP2+malP1v2+malP2v2;
		
	}
	public void incrementarNNENP2(Integer n, Integer numFurgoneta){
		//////System.out.println(" incrementarNNENP2");
		
		Integer iP=vFurgonetas[numFurgoneta].getindexEstacioP2();
		Integer iE=vFurgonetas[numFurgoneta].getindexEstacioE();
		
		
		
		Integer bienE=bicisBienColocadasIndexE(iE);
		Integer bienP2=bicisBienColocadasIndexE(iP);
		Integer malE=bicisMalColocadasIndexE(iE);
		Integer malP2=bicisMalColocadasIndexE(iP);
		Integer np2=vFurgonetas[numFurgoneta].getNp2();
		vFurgonetas[numFurgoneta].setNp2(np2+n);
		if(iP != -1)vEstaciones[iP].sumaNBicis(n);
		if(iE != -1)vEstaciones[iE].sumaNBicisAg(n);
		
		Integer bienEv2=bicisBienColocadasIndexE(iE);
		Integer bienP2v2=bicisBienColocadasIndexE(iP);
		Integer malEv2=bicisMalColocadasIndexE(iE);
		Integer malP2v2=bicisMalColocadasIndexE(iP);
		
		BicisBienColocadas= BicisBienColocadas-bienP2+bienP2v2-bienE+bienEv2;
		BicisMalColocadas= BicisMalColocadas-malE+malEv2-malP2+malP2v2;
		
	}
	public void decrementarNNENP2(Integer n, Integer numFurgoneta){
		//////System.out.println(" decrementarNNENP2");
		
		Integer iP=vFurgonetas[numFurgoneta].getindexEstacioP2();
		Integer iE=vFurgonetas[numFurgoneta].getindexEstacioE();
		
		Integer bienE=bicisBienColocadasIndexE(iE);
		Integer bienP2=bicisBienColocadasIndexE(iP);
		Integer malP2=bicisMalColocadasIndexE(iP);
		Integer malE=bicisMalColocadasIndexE(iE);
		
		vFurgonetas[numFurgoneta].setNp2(vFurgonetas[numFurgoneta].getNp2()-n);

		if(iP != -1)vEstaciones[iP].restaNBicis(n);
		if(iE != -1)vEstaciones[iE].restaNBicisAg(n);
		
		Integer bienEv2=bicisBienColocadasIndexE(iE);
		Integer bienP2v2=bicisBienColocadasIndexE(iP);
		Integer malEv2=bicisMalColocadasIndexE(iE);
		Integer malP2v2=bicisMalColocadasIndexE(iP);
		
		BicisBienColocadas=BicisBienColocadas-bienP2+bienP2v2-bienE+bienEv2;
		BicisMalColocadas=BicisMalColocadas-malE+malEv2-malP2+malP2v2;
		
	}
	public void decrementarNNENP1(Integer n, Integer numFurgoneta){
		//////System.out.println("  decrementarNNENP1");
		
		Integer iP=vFurgonetas[numFurgoneta].getindexEstacioP1();
		Integer iE=vFurgonetas[numFurgoneta].getindexEstacioE();
		
		Integer bienE=bicisBienColocadasIndexE(iE);
		Integer malP1=bicisMalColocadasIndexE(iP);
		Integer bienP1=bicisBienColocadasIndexE(iP);
		Integer malE=bicisMalColocadasIndexE(iE);
		
		vFurgonetas[numFurgoneta].setNp1(vFurgonetas[numFurgoneta].getNp1()-n);
		if(iP != -1)vEstaciones[iP].restaNBicis(n);
		if(iE != -1)vEstaciones[iE].restaNBicisAg(n);
		
		Integer bienEv2=bicisBienColocadasIndexE(iE);
		Integer malP1v2=bicisMalColocadasIndexE(iP);
		Integer bienP1v2=bicisBienColocadasIndexE(iP);
		Integer malEv2=bicisMalColocadasIndexE(iE);
		
		BicisBienColocadas=BicisBienColocadas-bienP1+bienP1v2-bienE+bienEv2;
		BicisMalColocadas=BicisMalColocadas-malE+malEv2-malP1+malP1v2;
		
	}
	public void intercambiarP1P2(Integer numFurgoneta){
		//////System.out.println("  intercambiarP1P2");
		
		Integer P1 = vFurgonetas[numFurgoneta].getindexEstacioP1();
		vFurgonetas[numFurgoneta].setindexEstacioP1(vFurgonetas[numFurgoneta].getindexEstacioP2());
		vFurgonetas[numFurgoneta].setindexEstacioP2(P1);
		
		Integer n=vFurgonetas[numFurgoneta].getNp1();
		vFurgonetas[numFurgoneta].setNp1(vFurgonetas[numFurgoneta].getNp2());
		vFurgonetas[numFurgoneta].setNp2(n);
	}
	/**
	 * PRE: EstacioP1 t� una o menys estacions assignades
	 * @param numFurgoneta
	 * @param EstacioP1
	 */
	public void canviarP1(Integer numFurgoneta, Integer EstacioP1){
		//////System.out.println("PATRIII dee");
		Integer P=vFurgonetas[numFurgoneta].getindexEstacioP1();
		Integer nP1=vFurgonetas[numFurgoneta].getNp1();
		//////System.out.println("PATRIII");
		Integer bienP1=bicisBienColocadasIndexE(P);
		//////System.out.println("PATRIII 2");
		Integer bienEP1=bicisBienColocadasIndexE(EstacioP1);
		//////System.out.println("PATRIII 3");
		Integer malP1=bicisMalColocadasIndexE(P);
		Integer malEP1=bicisMalColocadasIndexE(EstacioP1);
		
		if(EstacioP1 != -1)vEstaciones[EstacioP1].sumaNBicis(nP1);
		if(P != -1)vEstaciones[P].restaNBicis(nP1);
		// NO CANVIO LA FURGONETA PERQUE ES P1
		vFurgonetas[numFurgoneta].setindexEstacioP1(EstacioP1);
		
		Integer bienP1v2=bicisBienColocadasIndexE(P);
		Integer bienEP1v2=bicisBienColocadasIndexE(EstacioP1);
		Integer malP1v2=bicisMalColocadasIndexE(P);
		Integer malEP1v2=bicisMalColocadasIndexE(EstacioP1);
		
		
		BicisBienColocadas= BicisBienColocadas-bienP1-bienEP1+bienP1v2+bienEP1v2;
		BicisMalColocadas = BicisMalColocadas - malP1-malEP1 +malP1v2+malEP1v2;
		
	}
	public void canviarP2(Integer numFurgoneta, Integer EstacioP2){
		
		Integer P2=vFurgonetas[numFurgoneta].getindexEstacioP2();
		Integer nP2=vFurgonetas[numFurgoneta].getNp2();
		
		Integer bienP2=bicisBienColocadasIndexE(P2);
		Integer bienEP2=bicisBienColocadasIndexE(EstacioP2);
		Integer malP2=bicisMalColocadasIndexE(P2);
		Integer malEP2=bicisMalColocadasIndexE(EstacioP2);
		
		if(EstacioP2 != -1)vEstaciones[EstacioP2].sumaNBicis(nP2);
		if(P2 != -1)vEstaciones[P2].restaNBicis(nP2);
		// NO CANVIO LA FURGONETA PERQUE ES P2
		vFurgonetas[numFurgoneta].setindexEstacioP2(EstacioP2);
		
		Integer bienP2v2=bicisBienColocadasIndexE(P2);
		Integer bienEP2v2=bicisBienColocadasIndexE(EstacioP2);
		Integer malP2v2=bicisMalColocadasIndexE(P2);
		Integer malEP2v2=bicisMalColocadasIndexE(EstacioP2);
		
		
		BicisBienColocadas= BicisBienColocadas-bienP2-bienEP2+bienP2v2+bienEP2v2;
		BicisMalColocadas = BicisMalColocadas - malP2-malEP2 +malP2v2+malEP2v2;
		
	}
	
	
	
	public void canviarP1(Integer numFurgoneta, Estacion EstacioP1){
		//////System.out.println("  canviarP1");
		//////System.out.println(EstacioP1.getDemanda());
		Integer e1=GeneraProblema.getIndex(EstacioP1);
		canviarP1(numFurgoneta,e1);
		
		//////System.out.println( " bicis bien");
		//////System.out.println(BicisBienColocadas);
		//////System.out.println(BicisMalColocadas);
		
	}
	public void canviarP2(Integer numFurgoneta, Estacion EstacioP2){
		//////System.out.println("  canviarP2");
		Integer e2=GeneraProblema.getIndex(EstacioP2);
		canviarP2(numFurgoneta,e2);

	}
	

	private void ajustarNumero(Integer numFurgoneta, Integer index2){
		//System.out.println("  UUULL ");
		Integer b=0;
		if(vFurgonetas[numFurgoneta].getindexEstacioE() != -1){
			b=vFurgonetas[numFurgoneta].getEstacioE().getNumBicicletasNoUsadas();
		}
		//System.out.println("  UUULL 2");
		if(b<vFurgonetas[numFurgoneta].getNp1()+vFurgonetas[numFurgoneta].getNp2()){
			//System.out.println("  UUOUOUO ");
			
			Integer indexP1=vFurgonetas[numFurgoneta].getindexEstacioP1();
			Integer indexP2=vFurgonetas[numFurgoneta].getindexEstacioP2();
			Integer indexE=vFurgonetas[numFurgoneta].getindexEstacioE();
			//Integer bienE=bicisBienColocadasIndexE(indexE);
			//Integer malE=bicisMalColocadasIndexE(indexE);
			
			//System.out.println(" DDDD ");
			Integer bienP1=bicisBienColocadasIndexE(indexP1);
			Integer bienP2=bicisBienColocadasIndexE(indexP2);
			Integer malP1=bicisMalColocadasIndexE(indexP1);
			Integer malP2=bicisMalColocadasIndexE(indexP2);
			/**
			if((vFurgonetas[numFurgoneta].getEstacioE().getNumBicicletasNoUsadas())==0){
				
				Integer np1= vFurgonetas[numFurgoneta].getNp1();
				Integer np2= vFurgonetas[numFurgoneta].getNp2();
				vEstaciones[indexP1].restaNBicis(np1);
				vEstaciones[indexP2].restaNBicis(np2);
				vFurgonetas[numFurgoneta].setNp2(0);
				vFurgonetas[numFurgoneta].setNp1(0);
				vEstaciones[indexE].setBicisAgafen(0);
				
			}
			else{
			*/
			
			//System.out.println("  EEEE ");
				Integer n=(vFurgonetas[numFurgoneta].getNp1()+vFurgonetas[numFurgoneta].getNp2());
				Integer act=0;
				if(indexE != -1)
				{
					act=(vFurgonetas[numFurgoneta].getEstacioE().getNumBicicletasNoUsadas()) ;
				
					if(act > 30){
						act = 30;
					}
				}
				
				//System.out.println("  FFFF ");
				
				if(indexP1==indexP2){
					Integer np1=vFurgonetas[numFurgoneta].getNp1();
					Integer np2=vFurgonetas[numFurgoneta].getNp2();
					if(indexP1 != -1)vEstaciones[indexP1].restaNBicis(np1);
					if(indexP1 != -1)vEstaciones[indexP1].restaNBicis(np2);
					if(indexP1 != -1)vEstaciones[indexP1].sumaNBicis(act);
					vFurgonetas[numFurgoneta].setNp1(act);
					vFurgonetas[numFurgoneta].setNp2(0);
					////System.out.println("  EEEEppp ");
					if(indexE != -1)vEstaciones[indexE].setBicisAgafen(act);
				}
				else{
					Integer np1=vFurgonetas[numFurgoneta].getNp1();
					Integer np2=vFurgonetas[numFurgoneta].getNp2();
					vFurgonetas[numFurgoneta].setNp1(np1*act/n);
					vFurgonetas[numFurgoneta].setNp2(np2*act/n);
					
					Integer anp1=vFurgonetas[numFurgoneta].getNp1();
					Integer anp2=vFurgonetas[numFurgoneta].getNp2();
					Integer c1=0;
					if(indexP1!=-1)c1=vEstaciones[indexP1].getBicisColocades();
					
					if(indexP1 != -1)vEstaciones[indexP1].setBicisColocades(c1-np1+anp1);
					Integer c2=0;
					if(indexP2 != -1)vEstaciones[indexP2].getBicisColocades();
					if(indexP2 != -1)vEstaciones[indexP2].setBicisColocades(c2-np2+anp2);
					////System.out.println("  EEEE OOOOO");
					//////System.out.println(" np1" + np1 + " anp1 " +anp1 + " c1 " + c1);
					//////System.out.println(" np2" + np1 + " anp2 " +anp2 + " c2 " + c2);
					if(indexE != -1)vEstaciones[indexE].setBicisAgafen(anp1+anp2);
				}
			
				////System.out.println("  LLLL ");
			//Integer bienEv2=bicisBienColocadasIndexE(indexE);
			//Integer malEv2=bicisMalColocadasIndexE(indexE);
			Integer bienP1v2=bicisBienColocadasIndexE(indexP1);
			Integer bienP2v2=bicisBienColocadasIndexE(indexP2);
			Integer malP1v2=bicisMalColocadasIndexE(indexP1);
			Integer malP2v2=bicisMalColocadasIndexE(indexP2);
			
			
			if(indexE == indexP1){
				bienP1=0;
				malP1=0;
				bienP1v2=0;
				malP1v2=0;
			}
			/*
			if(indexP1 == indexP2){
				bienP2=0;
				malP2=0;
				bienP2v2=0;
				malP2v2=0;
			}
			*/
			if(indexE == indexP2){
				bienP2=0;
				malP2=0;
				bienP2v2=0;
				malP2v2=0;
			}
			
			
			if(index2 == indexP1){
				bienP1=0;
				malP1=0;
				bienP1v2=0;
				malP1v2=0;
			}
			
			if(indexP1 == indexP2){
				bienP2=0;
				malP2=0;
				bienP2v2=0;
				malP2v2=0;
			}
			
			if(index2 == indexP2){
				bienP2=0;
				malP2=0;
				bienP2v2=0;
				malP2v2=0;
			}
			
			
			//BicisMalColocadas=BicisMalColocadas-malE-malP1-malP2+malEv2+malP1v2+malP2v2;
			//BicisBienColocadas=BicisBienColocadas-bienE-bienP1-bienP2+bienEv2+bienP1v2+bienP2v2;
			BicisMalColocadas=BicisMalColocadas-malP1-malP2+malP1v2+malP2v2;
			BicisBienColocadas=BicisBienColocadas-bienP1-bienP2+bienP1v2+bienP2v2;
		}
		
		
	}
	/**
	public void intercambiarEs(Integer numFurgoneta1, Integer numFurgoneta2){
		//////System.out.println("  NOOOO");
		Integer EF1=vFurgonetas[numFurgoneta1].getindexEstacioE();
		Integer EF2=vFurgonetas[numFurgoneta2].getindexEstacioE();
		
		Integer bicisMalEF1bef=0;
		Integer bicisMalEF2bef=0;
		Integer bicisMalEF1now=0;
		Integer bicisMalEF2now=0;
		Integer neEF1=vFurgonetas[numFurgoneta1].getNp1()+vFurgonetas[numFurgoneta1].getNp2();
		Integer neEF2=vFurgonetas[numFurgoneta2].getNp1()+vFurgonetas[numFurgoneta2].getNp2();
		vFurgonetas[numFurgoneta1].setindexEstacioE(EF2);
		vFurgonetas[numFurgoneta2].setindexEstacioE(EF1);
		if(EF1 != -1){
			vEstaciones[EF1].setFurg(numFurgoneta2);
			bicisMalEF1bef=bicisMalColocadas(neEF1,EF1.getDemanda()-EF1.getNumBicicletasNext());
			
			ajustarNumero(numFurgoneta2);
			bicisMalEF1now=bicisMalColocadas(vFurgonetas[numFurgoneta2].getNp1()+vFurgonetas[numFurgoneta2].getNp2(),EF1.getDemanda()-EF1.getNumBicicletasNext());
		}
		if(EF2 != -1){
			int index2=es.indexOf(EF2);
			vEstaciones[index2].setFurg(numFurgoneta1);
			bicisMalEF2bef=bicisMalColocadas(neEF2,EF2.getDemanda()-EF2.getNumBicicletasNext());
			
			ajustarNumero(numFurgoneta1);
			bicisMalEF2now=bicisMalColocadas(vFurgonetas[numFurgoneta1].getNp1()+vFurgonetas[numFurgoneta1].getNp2(),EF2.getDemanda()-EF2.getNumBicicletasNext());
		}
		BicisMalColocadas= BicisMalColocadas+bicisMalEF2now+bicisMalEF1now-bicisMalEF1bef-bicisMalEF2bef;
	}
	**/
	
	private void intercambiarprivate(Integer index1,Integer index2){
		
		//////System.out.println("  NOOOO");
		
		if(index1!=index2){
			
			int fnum1=-1;
			if(index1 != -1)fnum1=vEstaciones[index1].getFurg();
			//System.out.println("HOOOLAAAA 2"+index1 + "HHHHH "+index2);
			int fnum2=-1;
			if(index2!=-1)fnum2=vEstaciones[index2].getFurg();
			
			
			
			Integer male1=bicisMalColocadasIndexE(index1);
			Integer male2=bicisMalColocadasIndexE(index2);
			Integer bien1=bicisBienColocadasIndexE(index1);
			Integer bien2=bicisBienColocadasIndexE(index2);
			
			Integer f1ag=0,f2ag=0;
			if(index1 != -1)f1ag=vEstaciones[index1].getBicisAgafen();
			if(index2 != -1)f2ag=vEstaciones[index2].getBicisAgafen();
			
			if(index1 != -1)vEstaciones[index1].setFurg(fnum2);
			if(index2 != -1)vEstaciones[index2].setFurg(fnum1);
			if(index1 != -1)vEstaciones[index1].setBicisAgafen(f2ag);
			if(index2 != -1)vEstaciones[index2].setBicisAgafen(f1ag);
			
			if(fnum1 != -1){
				vFurgonetas[fnum1].setindexEstacioE(index2);
				ajustarNumero(fnum1,index1);
			}
			if(fnum2 != -1){
				vFurgonetas[fnum2].setindexEstacioE(index1);
				ajustarNumero(fnum2,index2);
			}
	
			
			Integer male1v2=bicisMalColocadasIndexE(index1);
			Integer male2v2=bicisMalColocadasIndexE(index2);
			Integer bien1v2=bicisBienColocadasIndexE(index1);
			Integer bien2v2=bicisBienColocadasIndexE(index2);
			
			
			BicisMalColocadas= BicisMalColocadas-male1-male2+male1v2+male2v2;
			BicisBienColocadas= BicisBienColocadas-bien1-bien2+bien1v2+bien2v2;
		 
		}
		
	
	}
	/**
	 * Como m�nimo una de las dos estaciones tiene de tener una Furgoneta assignada
	 * @param numEstacion1
	 * @param numEstacion2
	 */
	/**
	public void intercambiarE(Estacion numEstacion1, Estacion numEstacion2){
		Estaciones es=GeneraProblema.getEstaciones();
		int index1=-1;
		if(numEstacion1 != null)index1=es.indexOf(numEstacion1);
		int index2=-1;
		if(numEstacion2 != null)index2=es.indexOf(numEstacion2);
		intercambiarprivate(numEstacion1,numEstacion2,index1,index2);
	}
	
	*/
	
	/**
	 * Como m�nimo una de las dos estaciones tiene de tener una Furgoneta assignada
	 * @param numEstacion1
	 * @param numEstacion2
	 */
	public void intercambiarE(Integer numEstacion1, Integer numEstacion2){
		intercambiarprivate(numEstacion1,numEstacion2);
	}
	
	private static int minim(int a,int b){
		
		if(a<b)return a;
		else return b;
		
	}
	
	
	
	
	public static Estado estadoInicial_v1(int numF, int numE){
		////System.out.println("proooofii  ");
		int min= minim(numF,numE);
		
		//////System.out.println(numE);
		//////System.out.println(numF);
		//////System.out.println(min);
		
		Integer BSuman=0;
		Integer Brestan=0;
		Double g=0.0;
		Furgoneta[] vfurg= new Furgoneta[numF];
		Struct[] vEst= new Struct[numE];
		Random rnd = new Random(GeneraProblema.getSemilla());
		
		Estaciones est=GeneraProblema.getEstaciones();
		java.util.ArrayList<Auxi> ve = new java.util.ArrayList<Auxi>();
		java.util.ArrayList<Auxi> vp = new java.util.ArrayList<Auxi>();
		////System.out.println("proooof 0");
		
		//////System.out.println("Jose");
		for(int i=0; i < numE; ++i)
		{
			Estacion e=GeneraProblema.getEstacion(i);
				Auxi a= new Auxi();
				a.setIndex(i);
				a.setSobren(e.getNumBicicletasNext()-e.getDemanda());
				ve.add(a);
				vEst[i]= new Struct();
			
			
		}
		////System.out.println("hiiii");
		Collections.sort(ve,(Comparator<Auxi>)ve.get(0));
		////System.out.println("hiiii 2");

		
		for(int i=0;i<min;++i) {
			
			//////System.out.println("mides 0");
			//////System.out.println(ve.size());
			//////System.out.println(vp.size());
			//////System.out.println(v2);
			
			////System.out.println("hiiii 3");
			Integer re=ve.get(i).getIndex();
			
			
			
			DosEst aux=GeneraProblema.estmesaprop(re);
			
			
			//////System.out.println("mides 1");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
			Estacion p1=aux.getA();
			Estacion p2=aux.getB();
			Integer rp1=GeneraProblema.getIndex(p1);
			Integer rp2=GeneraProblema.getIndex(p2);
			////System.out.println("niiii");
			////System.out.println(rp1);
			////System.out.println(rp2);
			//////System.out.println("mides 2");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
				
		
			
	
			//////System.out.println("mides 3");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
			
			Estacion e=GeneraProblema.getEstacion(re);

			Integer sobren=ve.get(i).getSobren();
			////System.out.println("SOOBREEEEN");
			////System.out.println(sobren);
			////System.out.println("hiiii 3.5");

			//Double n= 
				Integer ne= minim(e.getNumBicicletasNoUsadas(),sobren);
				if(ne<0)ne=0;
			
			////////System.out.println(n);
			ne=minim(30,ne);
			////////System.out.println(ne);
			int np1=ne/2;
			//////System.out.println(np1);
			int np2=ne-np1;
			////////System.out.println(np2);
			/**
			Integer bienE=bicisBienColocadasIndexE(re,vEst);
			Integer bienP1=bicisBienColocadasIndexE(rp1,vEst);
			Integer bienP2=bicisBienColocadasIndexE(rp2,vEst);
			Integer malP1=bicisMalColocadasIndexE(rp1,vEst);
			Integer malP2=bicisMalColocadasIndexE(rp2,vEst);
			*/
			////System.out.println("hiiii 3.7");
			
			vEst[rp1].sumaNBicis(np1);
			vEst[rp2].sumaNBicis(np2);
			////System.out.println("hiiii 3.8");
			vEst[re].setFurg(i);
			vEst[re].setBicisAgafen(ne);
			Furgoneta audi=new Furgoneta(re,rp1,rp2,np1,np2);
			////System.out.println("hiiii 4");
			vfurg[i]=audi;
			
			/**
			Integer bienEv2=bicisBienColocadasIndexE(re,vEst);
			Integer bienP1v2=bicisBienColocadasIndexE(rp1,vEst);
			Integer bienP2v2=bicisBienColocadasIndexE(rp2,vEst);
			Integer malP1v2=bicisMalColocadasIndexE(rp1,vEst);
			Integer malP2v2=bicisMalColocadasIndexE(rp2,vEst);
			
			Brestan =Brestan+bicisMalColocadasIndexE(re,vEst)-malP1-malP2+malP1v2+malP2v2;
			BSuman=BSuman-bienP1-bienP2-bienE+bienEv2+bienP1v2+bienP2v2-bienE+bienEv2;
			
			
			*/
			
			////////System.out.println("Vector9");
			Integer km1= GeneraProblema.distancia(e,p1);
			Integer km2= GeneraProblema.distancia(p1,p2);
			////////System.out.println(km1);
			////////System.out.println(km2);
			////////System.out.println("Vector9.5");
			Integer d1=(ne+9)/10;
			Integer d2=(np2+9)/10;
			////////System.out.println("Vector9.6");
			Double gau=(double) d1*km1+d2*km2;
			////////System.out.println("Vector9.7");
			
			////////System.out.println("Vector9.8");
			g=g+gau;
			////System.out.println("hiiii 5");
			//////System.out.println("Vector10");
			
		}
		//////System.out.println("Vector102");
		for (int i=min;i<numF;++i){
			//////System.out.println("Vector10w");
			vfurg[i]=new Furgoneta();
		}
		
		//////System.out.println("Vector12");
		for(int i=0; i<numE;++i){
			//////System.out.println("Bien" + i + " " + bicisBienColocadasIndexE(i,vEst));
			BSuman=BSuman+bicisBienColocadasIndexE(i,vEst);
		}
		//////System.out.println("Vector13");
		//////System.out.println(numF);
		for(int i=0; i<numF;++i){
			int re=vfurg[i].getindexEstacioE();
			if(re!=-1){
				//////System.out.println("Mal" + re + " " + bicisMalColocadasIndexE(re,vEst));
				Brestan =Brestan+bicisMalColocadasIndexE(re,vEst);
			}
		}
		//////System.out.println("Vector14");
		
		
		
		////////System.out.println("restan");
		//////System.out.println("restan" + Brestan);
		//////System.out.println("suman" + BSuman);
		
		////System.out.println("FIN ESTADO INICIAL");
		return new Estado(vfurg,vEst,g,BSuman,Brestan);
		

	}
	
	public static Estado estadoInicial_v2(int numF, int numE){
		////System.out.println("proooof");
		int min= minim(numF,numE);
		
		//////System.out.println(numE);
		//////System.out.println(numF);
		//////System.out.println(min);
		
		Integer BSuman=0;
		Integer Brestan=0;
		Double g=0.0;
		Furgoneta[] vfurg= new Furgoneta[numF];
		Struct[] vEst= new Struct[numE];
		Random rnd = new Random(GeneraProblema.getSemilla());
		
		Estaciones est=GeneraProblema.getEstaciones();
		java.util.ArrayList<Integer> ve = new java.util.ArrayList<Integer>();
		java.util.ArrayList<Integer> vp = new java.util.ArrayList<Integer>();
		
		
		
		//////System.out.println("Jose");
		for(int i=0; i < numE; ++i)
		{
			vEst[i]= new Struct();
			ve.add(i);
			vp.add(i);
			vp.add(i);
		}
		//////System.out.println("mides");
		////////System.out.println(ve.size());
		//////System.out.println(vp.size());
		
		
		for(int i=0;i<min;++i) {
			int re,rp1,rp2;
			//////System.out.println("mides 0");
			//////System.out.println(ve.size());
			//////System.out.println(vp.size());
			//////System.out.println(v2);
			
				//////System.out.println("hiii");
				int te;
				if(ve.size()==1)te=0;
				else te = rnd.nextInt(ve.size()-1);
				//////System.out.println(te);
				re = ve.get(te);
				
				ve.remove(te);
			
			//////System.out.println("mides 1");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
			int tp1=rnd.nextInt(vp.size()-1);
			rp1 = vp.get(tp1);
			
			//////System.out.println("mides 2");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
				
			int tp2;
			if(vp.size()==1)tp2=0;
			else tp2=rnd.nextInt(vp.size()-1);
			rp2 = vp.get(tp2);
			
	
			//////System.out.println("mides 3");
			////////System.out.println(ve.size());
			//////System.out.println(vp.size());
			
			Estacion e=GeneraProblema.getEstacion(re);
			Estacion p1=GeneraProblema.getEstacion(rp1);
			Estacion p2=GeneraProblema.getEstacion(rp2);
			
			
			

			//Double n= 
			Double n= 0.6*e.getNumBicicletasNoUsadas();
			
			
			////////System.out.println(n);
			Integer ne=n.intValue();
			ne=minim(30,ne);
			////////System.out.println(ne);
			int np1=ne/2;
			//////System.out.println(np1);
			int np2=ne-np1;
			////////System.out.println(np2);
			/**
			Integer bienE=bicisBienColocadasIndexE(re,vEst);
			Integer bienP1=bicisBienColocadasIndexE(rp1,vEst);
			Integer bienP2=bicisBienColocadasIndexE(rp2,vEst);
			Integer malP1=bicisMalColocadasIndexE(rp1,vEst);
			Integer malP2=bicisMalColocadasIndexE(rp2,vEst);
			*/
			
			vEst[rp1].sumaNBicis(np1);
			vEst[rp2].sumaNBicis(np2);
			
			vEst[re].setFurg(i);
			vEst[re].setBicisAgafen(ne);
			Furgoneta aux=new Furgoneta(re,rp1,rp2,np1,np2);
			
			vfurg[i]=aux;
			/**
			Integer bienEv2=bicisBienColocadasIndexE(re,vEst);
			Integer bienP1v2=bicisBienColocadasIndexE(rp1,vEst);
			Integer bienP2v2=bicisBienColocadasIndexE(rp2,vEst);
			Integer malP1v2=bicisMalColocadasIndexE(rp1,vEst);
			Integer malP2v2=bicisMalColocadasIndexE(rp2,vEst);
			
			Brestan =Brestan+bicisMalColocadasIndexE(re,vEst)-malP1-malP2+malP1v2+malP2v2;
			BSuman=BSuman-bienP1-bienP2-bienE+bienEv2+bienP1v2+bienP2v2-bienE+bienEv2;
			
			*/
			
			
			////////System.out.println("Vector9");
			Integer km1= GeneraProblema.distancia(e,p1);
			Integer km2= GeneraProblema.distancia(p1,p2);
			////////System.out.println(km1);
			////////System.out.println(km2);
			////////System.out.println("Vector9.5");
			Integer d1=(ne+9)/10;
			Integer d2=(np2+9)/10;
			////////System.out.println("Vector9.6");
			Double gau=(double) d1*km1+d2*km2;
			////////System.out.println("Vector9.7");
			
			////////System.out.println("Vector9.8");
			g=g+gau;
			//////System.out.println("Vector10");
			
		}
		//////System.out.println("Vector102");
		for (int i=min;i<numF;++i){
			//////System.out.println("Vector10w");
			vfurg[i]=new Furgoneta();
		}
		
		//////System.out.println("Vector12");
		for(int i=0; i<numE;++i){
			//////System.out.println("Bien" + i + " " + bicisBienColocadasIndexE(i,vEst));
			BSuman=BSuman+bicisBienColocadasIndexE(i,vEst);
		}
		//////System.out.println("Vector13");
		//////System.out.println(numF);
		for(int i=0; i<numF;++i){
			int re=vfurg[i].getindexEstacioE();
			if(re!=-1){
				//////System.out.println("Mal" + re + " " + bicisMalColocadasIndexE(re,vEst));
				Brestan =Brestan+bicisMalColocadasIndexE(re,vEst);
			}
		}
		//////System.out.println("Vector14");
		
		
		
		////////System.out.println("restan");
		//////System.out.println("restan" + Brestan);
		////System.out.println("FIN ESTADO INICIAL");
		

		return new Estado(vfurg,vEst,g,BSuman,Brestan);
		


	}
	

	
	
	public void print() {
		
		System.out.println("Furgonetas:");
		for (Furgoneta f : vFurgonetas) {
			Integer E = f.getindexEstacioE(), P1 = f.getindexEstacioP1(), P2 = f.getindexEstacioP2(); 
			int np1 = f.getNp1(), np2 = f.getNp2();
			Estaciones es=GeneraProblema.getEstaciones();
			System.out.println("Inicial:" + E + " " + (np1+np2) + " P1: " + P1+ " " + np1 + " P2: " + P2+ " " + np2);
		}
		System.out.println("");
		
	}
	
	
}

