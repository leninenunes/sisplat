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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "apropriacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Apropriacao.findAll", query = "SELECT a FROM Apropriacao a")
    , @NamedQuery(name = "Apropriacao.findById", query = "SELECT a FROM Apropriacao a WHERE a.id = :id")
    , @NamedQuery(name = "Apropriacao.findByData", query = "SELECT a FROM Apropriacao a WHERE a.data = :data")
    , @NamedQuery(name = "Apropriacao.findByInicio", query = "SELECT a FROM Apropriacao a WHERE a.inicio = :inicio")
    , @NamedQuery(name = "Apropriacao.findByTermino", query = "SELECT a FROM Apropriacao a WHERE a.termino = :termino")
    , @NamedQuery(name = "Apropriacao.findByIntervalo", query = "SELECT a FROM Apropriacao a WHERE a.intervalo = :intervalo")
    , @NamedQuery(name = "Apropriacao.findByTag", query = "SELECT a FROM Apropriacao a WHERE a.tag = :tag")})
public class Apropriacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Basic(optional = false)
    @Column(name = "inicio")
    @Temporal(TemporalType.TIME)
    private Date inicio;
    @Basic(optional = false)
    @Column(name = "termino")
    @Temporal(TemporalType.TIME)
    private Date termino;
    @Basic(optional = false)
    @Column(name = "intervalo")
    private double intervalo;
    @Column(name = "tag")
    private String tag;
    @JoinColumn(name = "escopo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Escopo escopoId;
    @JoinColumn(name = "profissional_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profissional profissionalId;

    public Apropriacao() {
    }

    public Apropriacao(Integer id) {
        this.id = id;
    }

    public Apropriacao(Integer id, Date data, Date inicio, Date termino, double intervalo) {
        this.id = id;
        this.data = data;
        this.inicio = inicio;
        this.termino = termino;
        this.intervalo = intervalo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public double getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(double intervalo) {
        this.intervalo = intervalo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Escopo getEscopoId() {
        return escopoId;
    }

    public void setEscopoId(Escopo escopoId) {
        this.escopoId = escopoId;
    }

    public Profissional getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Profissional profissionalId) {
        this.profissionalId = profissionalId;
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
        if (!(object instanceof Apropriacao)) {
            return false;
        }
        Apropriacao other = (Apropriacao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Apropriacao[ id=" + id + " ]";
    }
    
}
