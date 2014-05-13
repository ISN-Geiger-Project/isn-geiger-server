package isn.geiger.server.database.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author LoadLow
 */
@Entity
@Table(name = "hitlogs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hitlog.findAll", query = "SELECT h FROM Hitlog h"),
    @NamedQuery(name = "Hitlog.findByTime", query = "SELECT h FROM Hitlog h WHERE h.time = :time"),
    @NamedQuery(name = "Hitlog.findByCount", query = "SELECT h FROM Hitlog h WHERE h.count = :count")})
public class Hitlog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Basic(optional = false)
    @Column(name = "count")
    private int count;

    public Hitlog() {
    }

    public Hitlog(Date time) {
        this.time = time;
    }

    public Hitlog(Date time, int count) {
        this.time = time;
        this.count = count;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (time != null ? time.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hitlog)) {
            return false;
        }
        Hitlog other = (Hitlog) object;
        if ((this.time == null && other.time != null) || (this.time != null && !this.time.equals(other.time))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isn.geiger.server.database.entities.Hitlog[ time=" + time + " ]";
    }
}
