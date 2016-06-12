package nl.tma.handlers;

/**
 * Created by Wouter on 6/12/2016.
 */
public interface ReportProcessor {
    public void processSonarReport(String jsonAsString) throws Exception;
}
