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
@Table(name = "unidade_medida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeMedida.findAll", query = "SELECT u FROM UnidadeMedida u")
    , @NamedQuery(name = "UnidadeMedida.findById", query = "SELECT u FROM UnidadeMedida u WHERE u.id = :id")
    , @NamedQuery(name = "UnidadeMedida.findByNome", query = "SELECT u FROM UnidadeMedida u WHERE u.nome = :nome")
    , @NamedQuery(name = "UnidadeMedida.findByDescricao", query = "SELECT u FROM UnidadeMedida u WHERE u.descricao = :descricao")})
public class UnidadeMedida implements Serializable {

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
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadeMedidaId")
    private Collection<Escopo> escopoCollection;

    public UnidadeMedida() {
    }

    public UnidadeMedida(Integer id) {
        this.id = id;
    }

    public UnidadeMedida(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        if (!(object instanceof UnidadeMedida)) {
            return false;
        }
        UnidadeMedida other = (UnidadeMedida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UnidadeMedida[ id=" + id + " ]";
    }
    
}
