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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "escopo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escopo.findAll", query = "SELECT e FROM Escopo e")
    , @NamedQuery(name = "Escopo.findById", query = "SELECT e FROM Escopo e WHERE e.id = :id")
    , @NamedQuery(name = "Escopo.findByDescricao", query = "SELECT e FROM Escopo e WHERE e.descricao = :descricao")
    , @NamedQuery(name = "Escopo.findByQuantidade", query = "SELECT e FROM Escopo e WHERE e.quantidade = :quantidade")
    , @NamedQuery(name = "Escopo.findByValor", query = "SELECT e FROM Escopo e WHERE e.valor = :valor")})
public class Escopo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private Double quantidade;
    @Basic(optional = false)
    @Column(name = "valor")
    private Double valor;
    @JoinColumn(name = "contrato_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Contrato contratoId;
    @JoinColumn(name = "unidade_medida_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnidadeMedida unidadeMedidaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escopoId")
    private Collection<Apropriacao> apropriacaoCollection;

    public Escopo() {
    }

    public Escopo(Integer id) {
        this.id = id;
    }

    public Escopo(Integer id, String descricao, Double quantidade, Double valor) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Contrato getContratoId() {
        return contratoId;
    }
    
    public String getContratoDescricao(){
        return contratoId.getDescricao();
    }

    public void setContratoId(Contrato contratoId) {
        this.contratoId = contratoId;
    }

    public UnidadeMedida getUnidadeMedidaId() {
        return unidadeMedidaId;
    }
    
    public String getUnidadeMedidaNome(){
        return unidadeMedidaId.getNome();
    }

    public void setUnidadeMedidaId(UnidadeMedida unidadeMedidaId) {
        this.unidadeMedidaId = unidadeMedidaId;
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
        if (!(object instanceof Escopo)) {
            return false;
        }
        Escopo other = (Escopo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Escopo[ id=" + id + " ]";
    }
    
}
