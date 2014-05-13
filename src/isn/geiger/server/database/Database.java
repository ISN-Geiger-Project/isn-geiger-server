
package isn.geiger.server.database;

import isn.geiger.server.database.controllers.HitlogJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author LoadLow
 */
public class Database {
    
    public final HitlogJpaController HitLogController;
    
    private final EntityManagerFactory emf;
    
    public Database(String emfName){
        this.emf = Persistence.createEntityManagerFactory(emfName);
        this.HitLogController = new HitlogJpaController(emf);
    }
    
}
