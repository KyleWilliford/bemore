package net.kpw.bemore;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.kpw.bemore.resources.BeMoreResource;

public class BeMoreApplication extends Application<BeMoreConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BeMoreApplication().run(args);
    }

    @Override
    public String getName() {
        return "BeMore";
    }

    @Override
    public void initialize(final Bootstrap<BeMoreConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final BeMoreConfiguration configuration,
                    final Environment environment) {
        final BeMoreResource beMoreResource = new BeMoreResource();
        environment.jersey().register(beMoreResource);
    }

}
