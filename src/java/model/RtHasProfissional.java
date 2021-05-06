/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lenine.nunes
 */
@Entity
@Table(name = "rt_has_profissional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RtHasProfissional.findAll", query = "SELECT r FROM RtHasProfissional r")
    , @NamedQuery(name = "RtHasProfissional.findById", query = "SELECT r FROM RtHasProfissional r WHERE r.rtHasProfissionalPK.id = :id")
    , @NamedQuery(name = "RtHasProfissional.findByRtId", query = "SELECT r FROM RtHasProfissional r WHERE r.rtHasProfissionalPK.rtId = :rtId")
    , @NamedQuery(name = "RtHasProfissional.findByProfissionalId", query = "SELECT r FROM RtHasProfissional r WHERE r.rtHasProfissionalPK.profissionalId = :profissionalId")})
public class RtHasProfissional implements Serializable {

    @Basic(optional = false)
    @Column(name = "status")
    private Integer status;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RtHasProfissionalPK rtHasProfissionalPK;
    @JoinColumn(name = "profissional_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Profissional profissional;
    @JoinColumn(name = "rt_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Rt rt;
//    @JoinColumn(name = "status_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Status statusId;

    public RtHasProfissional() {
    }

    public RtHasProfissional(RtHasProfissionalPK rtHasProfissionalPK) {
        this.rtHasProfissionalPK = rtHasProfissionalPK;
    }

    public RtHasProfissional(int id, int rtId, int profissionalId) {
        this.rtHasProfissionalPK = new RtHasProfissionalPK(id, rtId, profissionalId);
    }

    public RtHasProfissionalPK getRtHasProfissionalPK() {
        return rtHasProfissionalPK;
    }

    public void setRtHasProfissionalPK(RtHasProfissionalPK rtHasProfissionalPK) {
        this.rtHasProfissionalPK = rtHasProfissionalPK;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Rt getRt() {
        return rt;
    }

    public void setRt(Rt rt) {
        this.rt = rt;
    }
    
    public String getStatusBundle(){
        String statusBundle = "";
        if(this.status != null){
            switch(this.status){
                case 1:
                    statusBundle = "RtStatusProgramado";
                    break;
                case 2:
                    statusBundle = "RtStatusLiberado";
                    break;
                case 3:
                    statusBundle = "RtStatusCancelado";
                    break;
            }
        }
        return statusBundle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rtHasProfissionalPK != null ? rtHasProfissionalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RtHasProfissional)) {
            return false;
        }
        RtHasProfissional other = (RtHasProfissional) object;
        if ((this.rtHasProfissionalPK == null && other.rtHasProfissionalPK != null) || (this.rtHasProfissionalPK != null && !this.rtHasProfissionalPK.equals(other.rtHasProfissionalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.RtHasProfissional[ rtHasProfissionalPK=" + rtHasProfissionalPK + " ]";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
}
