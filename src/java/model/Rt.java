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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "rt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rt.findAll", query = "SELECT r FROM Rt r")
    , @NamedQuery(name = "Rt.findById", query = "SELECT r FROM Rt r WHERE r.id = :id")
    , @NamedQuery(name = "Rt.findByTipo", query = "SELECT r FROM Rt r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "Rt.findByDataViagem", query = "SELECT r FROM Rt r WHERE r.dataViagem = :dataViagem")
    , @NamedQuery(name = "Rt.findByHoraViagem", query = "SELECT r FROM Rt r WHERE r.horaViagem = :horaViagem")})
public class Rt implements Serializable {

    @Lob
    @Column(name = "comentario")
    private String comentario;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo")
    private Integer tipo;
    @Basic(optional = false)
    @Column(name = "data_viagem")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataViagem;
    @Basic(optional = false)
    @Column(name = "hora_viagem")
    @Temporal(TemporalType.TIME)
    private Date horaViagem;
    @JoinColumn(name = "local_embarque_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Local localEmbarqueId;
    @JoinColumn(name = "local_desembarque_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Local localDesembarqueId;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status statusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rt")
    private Collection<RtHasProfissional> rtHasProfissionalCollection;

    public Rt() {
    }

    public Rt(Integer id) {
        this.id = id;
    }

    public Rt(Integer id, Integer tipo, Date dataViagem, Date horaViagem) {
        this.id = id;
        this.tipo = tipo;
        this.dataViagem = dataViagem;
        this.horaViagem = horaViagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    public String getTipoNome(){
        String tipoNome = "";
        if(this.tipo != null){
            switch (this.tipo) {
                case 1 :
                    tipoNome = "EMBARQUE";
                    break;
                case 2 :
                    tipoNome = "DESEMBARQUE";
                    break;
                case 3 :
                    tipoNome = "TRANSFERÊNCIA";
                    break;
            }
        }
        return tipoNome;
    }
    
    public Date getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(Date dataViagem) {
        this.dataViagem = dataViagem;
    }

    public Date getHoraViagem() {
        return horaViagem;
    }

    public void setHoraViagem(Date horaViagem) {
        this.horaViagem = horaViagem;
    }


    public Local getLocalEmbarqueId() {
        return localEmbarqueId;
    }

    public void setLocalEmbarqueId(Local localEmbarqueId) {
        this.localEmbarqueId = localEmbarqueId;
    }

    public Local getLocalDesembarqueId() {
        return localDesembarqueId;
    }

    public void setLocalDesembarqueId(Local localDesembarqueId) {
        this.localDesembarqueId = localDesembarqueId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    @XmlTransient
    public Collection<RtHasProfissional> getRtHasProfissionalCollection() {
        return rtHasProfissionalCollection;
    }

    public void setRtHasProfissionalCollection(Collection<RtHasProfissional> rtHasProfissionalCollection) {
        this.rtHasProfissionalCollection = rtHasProfissionalCollection;
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
        if (!(object instanceof Rt)) {
            return false;
        }
        Rt other = (Rt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " - " + getTipoNome() + " - " + dataViagem;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
