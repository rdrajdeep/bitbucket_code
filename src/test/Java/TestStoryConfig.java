// Created by Kishor on 1/24/2017.

import com.google.gson.Gson;
import com.relevantcodes.extentreports.ExtentReports;
import expertchat.bdd.E2ETestCase;
import expertchat.report.Report;
import expertchat.usermap.TestUserMap;
import expertchat.util.Email;
import expertchat.util.ExpertChatException;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.StoryTimeouts;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.AfterClass;
import org.junit.Before;

import java.util.List;

import static expertchat.util.ExpertChatUtility.getValue;

/* Story configurator*/

public class TestStoryConfig extends JUnitStories {

    private ExtentReports extent = null;

    @AfterClass
    public static void sendEmailNotification ( ) {

                /*After Test Execution send email notification*/
        Email.sendEmail ( getValue ( "EmailBody" ), getValue ( "From" ),
                getValue ( "To" ), getValue ( "subject" ), true );

                /*After Test execution create json*/
        System.out.println ( new Gson ( ).toJson ( TestUserMap.getMap ( ) ).toString ( ) );

    }

    @Before
    public void setReports ( ) {

        extent = new ExtentReports ( Report.rPath, false );

        if ( extent == null ) {
            throw new ExpertChatException ( "Something serious has just happened!!!!!" );
        }
    }

    public ExtentReports getReport ( ) {

        if ( extent != null ) {

            return extent;
        }
        return null;
    }

    @Override
    public Embedder configuredEmbedder ( ) {

        final EmbedderControls embedderControls = new EmbedderControls ( );
        embedderControls.ignoreFailureInStories ( );
        embedderControls.doVerboseFailures ( false );
        embedderControls.ignoreFailureInView ( );
        embedderControls.useThreads ( 1 );
        embedderControls.failOnStoryTimeout ( );

        final Embedder embedder = new Embedder ( );
        final Configuration configuration = configuration ( );

        StoryTimeouts.TimeoutParser t = new StoryTimeouts.TimeoutParser ( ) {
            @Override
            public boolean isValid ( String timeout ) {
                return true;
            }

            @Override
            public long asSeconds ( String timeout ) {
                return 500;
            }
        };

        embedder.useTimeoutParsers ( t );
        embedder.useConfiguration ( configuration );
        embedder.useStepsFactory ( stepsFactory ( ) );

        return embedder;
    }

    @Override
    public Configuration configuration ( ) {

        return new MostUsefulConfiguration ( )
                .useStoryLoader ( new LoadFromClasspath ( this.getClass ( ) ) );
    }

    @Override
    public List < String > storyPaths ( ) {

        List < String > s = new StoryFinder ( ).
                findPaths ( CodeLocations.codeLocationFromClass ( this.getClass ( ) ),
                        "**/*.story", null );
        return s;
    }

    @Override
    public InjectableStepsFactory stepsFactory ( ) {

        return new InstanceStepsFactory ( configuration ( ),
                new E2ETestCase ( getReport ( ), "End-to-End Scenario" )
        );
    }
}