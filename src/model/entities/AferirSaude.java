package model.entities;

import java.io.Serializable;
import java.util.Date;

public class AferirSaude implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Double pressaoArterialSistolica;
	private Double pressaoArterialDiastolica;
	private Double pulsacao;
	private Double temperaturaCorporal;
	private Double imc;
	private Date dataAfericao;
	private Boolean sintomas;
	
	private Mergulhador mergulhador;

	public AferirSaude(Integer id, Double pressaoArterialSistolica, Double pressaoArterialDiastolica, Double pulsacao,
			Double temperaturaCorporal, Double imc, Date dataAfericao, Boolean sintomas, Mergulhador mergulhador) {
		super();
		this.id = id;
		this.pressaoArterialSistolica = pressaoArterialSistolica;
		this.pressaoArterialDiastolica = pressaoArterialDiastolica;
		this.pulsacao = pulsacao;
		this.temperaturaCorporal = temperaturaCorporal;
		this.imc = imc;
		this.dataAfericao = dataAfericao;
		this.sintomas = sintomas;
		this.mergulhador = mergulhador;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPressaoArterialSistolica() {
		return pressaoArterialSistolica;
	}

	public void setPressaoArterialSistolica(Double pressaoArterialSistolica) {
		this.pressaoArterialSistolica = pressaoArterialSistolica;
	}

	public Double getPressaoArterialDiastolica() {
		return pressaoArterialDiastolica;
	}

	public void setPressaoArterialDiastolica(Double pressaoArterialDiastolica) {
		this.pressaoArterialDiastolica = pressaoArterialDiastolica;
	}

	public Double getPulsacao() {
		return pulsacao;
	}

	public void setPulsacao(Double pulsacao) {
		this.pulsacao = pulsacao;
	}

	public Double getTemperaturaCorporal() {
		return temperaturaCorporal;
	}

	public void setTemperaturaCorporal(Double temperaturaCorporal) {
		this.temperaturaCorporal = temperaturaCorporal;
	}

	public Double getImc() {
		return imc;
	}

	public void setImc(Double imc) {
		this.imc = imc;
	}

	public Date getDataAfericao() {
		return dataAfericao;
	}

	public void setDataAfericao(Date dataAfericao) {
		this.dataAfericao = dataAfericao;
	}

	public Boolean getSintomas() {
		return sintomas;
	}

	public void setSintomas(Boolean sintomas) {
		this.sintomas = sintomas;
	}

	public Mergulhador getMergulhador() {
		return mergulhador;
	}

	public void setMergulhador(Mergulhador mergulhador) {
		this.mergulhador = mergulhador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AferirSaude other = (AferirSaude) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AferirSaude [id=" + id + ", pressaoArterialSistolica=" + pressaoArterialSistolica
				+ ", pressaoArterialDiastolica=" + pressaoArterialDiastolica + ", pulsacao=" + pulsacao
				+ ", temperaturaCorporal=" + temperaturaCorporal + ", imc=" + imc + ", dataAfericao=" + dataAfericao
				+ ", sintomas=" + sintomas + ", mergulhador=" + mergulhador + "]";
	}

	

	
	
}
