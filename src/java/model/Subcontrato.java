/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenine.nunes
 */
@Entity
@Table(name = "subcontrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subcontrato.findAll", query = "SELECT s FROM Subcontrato s")
    , @NamedQuery(name = "Subcontrato.findById", query = "SELECT s FROM Subcontrato s WHERE s.id = :id")
    , @NamedQuery(name = "Subcontrato.findByNumero", query = "SELECT s FROM Subcontrato s WHERE s.numero = :numero")
    , @NamedQuery(name = "Subcontrato.findByDescricao", query = "SELECT s FROM Subcontrato s WHERE s.descricao = :descricao")
    , @NamedQuery(name = "Subcontrato.findByPrazo", query = "SELECT s FROM Subcontrato s WHERE s.prazo = :prazo")
    , @NamedQuery(name = "Subcontrato.findByInicio", query = "SELECT s FROM Subcontrato s WHERE s.inicio = :inicio")
    , @NamedQuery(name = "Subcontrato.findByValor", query = "SELECT s FROM Subcontrato s WHERE s.valor = :valor")})
public class Subcontrato implements Serializable {

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

    public Subcontrato() {
    }

    public Subcontrato(Integer id) {
        this.id = id;
    }

    public Subcontrato(Integer id, int numero, String descricao, int prazo, Date inicio, double valor) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subcontrato)) {
            return false;
        }
        Subcontrato other = (Subcontrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Subcontrato[ id=" + id + " ]";
    }
    
}
