package net.kpw.bemore;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.kpw.bemore.dao.DataDAO;
import net.kpw.bemore.model.Data;
import net.kpw.bemore.resource.BeMoreResource;

public class BeMoreApplication extends Application<BeMoreConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BeMoreApplication().run(args);
    }

    @Override
    public String getName() {
        return "BeMore";
    }
    
    private final HibernateBundle<BeMoreConfiguration> hibernate = new HibernateBundle<BeMoreConfiguration>(Data.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(BeMoreConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(final Bootstrap<BeMoreConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final BeMoreConfiguration configuration,
                    final Environment environment) {

        // DAOs
        final DataDAO dataDao = new DataDAO(hibernate.getSessionFactory());
        
        // Resources
        final BeMoreResource beMoreResource = new BeMoreResource(dataDao);
        environment.jersey().register(beMoreResource);
    }

}
