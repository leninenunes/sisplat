/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author lenine.nunes
 */
@Embeddable
public class RtHasProfissionalPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "rt_id")
    private int rtId;
    @Basic(optional = false)
    @Column(name = "profissional_id")
    private int profissionalId;

    public RtHasProfissionalPK() {
    }

    public RtHasProfissionalPK(int id, int rtId, int profissionalId) {
        this.id = id;
        this.rtId = rtId;
        this.profissionalId = profissionalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRtId() {
        return rtId;
    }

    public void setRtId(int rtId) {
        this.rtId = rtId;
    }

    public int getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(int profissionalId) {
        this.profissionalId = profissionalId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) rtId;
        hash += (int) profissionalId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RtHasProfissionalPK)) {
            return false;
        }
        RtHasProfissionalPK other = (RtHasProfissionalPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.rtId != other.rtId) {
            return false;
        }
        if (this.profissionalId != other.profissionalId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.RtHasProfissionalPK[ id=" + id + ", rtId=" + rtId + ", profissionalId=" + profissionalId + " ]";
    }
    
}
