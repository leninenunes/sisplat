/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lenine.nunes
 */
@Entity
@Table(name = "profissional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profissional.findAll", query = "SELECT p FROM Profissional p")
    , @NamedQuery(name = "Profissional.findById", query = "SELECT p FROM Profissional p WHERE p.id = :id")
    , @NamedQuery(name = "Profissional.findByNome", query = "SELECT p FROM Profissional p WHERE p.nome = :nome")
    , @NamedQuery(name = "Profissional.findByMatricula", query = "SELECT p FROM Profissional p WHERE p.matricula = :matricula")})
public class Profissional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "matricula")
    private String matricula;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profissional")
    private Collection<RtHasProfissional> rtHasProfissionalCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profissionalId")
    private Collection<Apropriacao> apropriacaoCollection;

    public Profissional() {
    }

    public Profissional(Integer id) {
        this.id = id;
    }

    public Profissional(Integer id, String nome, String matricula) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @XmlTransient
    public Collection<RtHasProfissional> getRtHasProfissionalCollection() {
        return rtHasProfissionalCollection;
    }

    public void setRtHasProfissionalCollection(Collection<RtHasProfissional> rtHasProfissionalCollection) {
        this.rtHasProfissionalCollection = rtHasProfissionalCollection;
    }

    @XmlTransient
    public Collection<Apropriacao> getApropriacaoCollection() {
        return apropriacaoCollection;
    }

    public void setApropriacaoCollection(Collection<Apropriacao> apropriacaoCollection) {
        this.apropriacaoCollection = apropriacaoCollection;
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
        if (!(object instanceof Profissional)) {
            return false;
        }
        Profissional other = (Profissional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome + " - " + matricula;
    }
    
}
