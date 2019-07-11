package it.polito.tdp.poweroutages.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {

	private List<Nerc> nerc;
	private PowerOutagesDAO dao;

	private SimpleWeightedGraph<Nerc, DefaultWeightedEdge> grafo;

	public Model() {
		dao = new PowerOutagesDAO();
		this.nerc = dao.loadAllNercs();
	}

	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<Nerc, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(this.grafo, nerc);

		for(Nerc n1 : this.grafo.vertexSet()) {
			for(Nerc n2 : this.grafo.vertexSet()) {

				if(dao.getArchi(n1, n2)) {
					// aggiungi l'arco
					this.grafo.addEdge(n1, n2);
				}

			}

		}

		System.out.println("Grafo creato!");
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());

		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Nerc sorgente = this.grafo.getEdgeSource(e);
			Nerc destinazione = this.grafo.getEdgeTarget(e);

			int peso = dao.getPeso(sorgente, destinazione);
			// if(peso>0) { // DA NON CONSIDERARE PESO>0
				this.grafo.setEdgeWeight(e, peso);
			// }

		}


	}

	public List<Nerc> getNerc() {
		return this.nerc;
	}

	public List<Vicini> getVicini(Nerc nerc2) {

		List<Vicini> vicini = new LinkedList<Vicini>();

		for(DefaultWeightedEdge e : this.grafo.edgesOf(nerc2)) {
			Nerc vertice1 = this.grafo.getEdgeSource(e);
			Nerc vertice2 = this.grafo.getEdgeTarget(e);
			int peso = (int) this.grafo.getEdgeWeight(e);
			Vicini vicino;
			if(vertice1.getId()!=nerc2.getId()) {
				vicino = new Vicini(vertice1, peso);
			}
			else {
				vicino = new Vicini(vertice2, peso);
			}

			
			vicini.add(vicino);
		}

		Collections.sort(vicini);

		return vicini;
	}

}
