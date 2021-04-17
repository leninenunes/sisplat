/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenine.nunes
 */
@Entity
@Table(name = "contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findAll", query = "SELECT c FROM Contrato c")
    , @NamedQuery(name = "Contrato.findById", query = "SELECT c FROM Contrato c WHERE c.id = :id")
    , @NamedQuery(name = "Contrato.findByNumero", query = "SELECT c FROM Contrato c WHERE c.numero = :numero")
    , @NamedQuery(name = "Contrato.findByDescricao", query = "SELECT c FROM Contrato c WHERE c.descricao = :descricao")
    , @NamedQuery(name = "Contrato.findByPrazo", query = "SELECT c FROM Contrato c WHERE c.prazo = :prazo")
    , @NamedQuery(name = "Contrato.findByInicio", query = "SELECT c FROM Contrato c WHERE c.inicio = :inicio")
    , @NamedQuery(name = "Contrato.findByValor", query = "SELECT c FROM Contrato c WHERE c.valor = :valor")})
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "prazo")
    private int prazo;
    @Basic(optional = false)
    @Column(name = "inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    @Basic(optional = false)
    @Column(name = "valor")
    private double valor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratoId")
    private Collection<Escopo> escopoCollection;

    public Contrato() {
    }

    public Contrato(Integer id) {
        this.id = id;
    }

    public Contrato(Integer id, int numero, String descricao, int prazo, Date inicio, double valor) {
        this.id = id;
        this.numero = numero;
        this.descricao = descricao;
        this.prazo = prazo;
        this.inicio = inicio;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @XmlTransient
    public Collection<Escopo> getEscopoCollection() {
        return escopoCollection;
    }

    public void setEscopoCollection(Collection<Escopo> escopoCollection) {
        this.escopoCollection = escopoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Contrato[ id=" + id + " ]";
    }
    
}
