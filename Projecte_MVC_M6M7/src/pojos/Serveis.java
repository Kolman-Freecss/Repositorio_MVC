package pojos;
// Generated 22-feb-2018 17:24:00 by Hibernate Tools 4.0.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Serveis generated by hbm2java
 */
public class Serveis implements java.io.Serializable {

	private int codi;
	private String descripcio;
	private Set<Assistencies> assistencieses = new HashSet<Assistencies>(0);

	public Serveis() {
	}

	public Serveis(int codi, String descripcio) {
		this.codi = codi;
		this.descripcio = descripcio;
	}

	public Serveis(int codi, String descripcio, Set<Assistencies> assistencieses) {
		this.codi = codi;
		this.descripcio = descripcio;
		this.assistencieses = assistencieses;
	}

	public int getCodi() {
		return this.codi;
	}

	public void setCodi(int codi) {
		this.codi = codi;
	}

	public String getDescripcio() {
		return this.descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public Set<Assistencies> getAssistencieses() {
		return this.assistencieses;
	}

	public void setAssistencieses(Set<Assistencies> assistencieses) {
		this.assistencieses = assistencieses;
	}

}