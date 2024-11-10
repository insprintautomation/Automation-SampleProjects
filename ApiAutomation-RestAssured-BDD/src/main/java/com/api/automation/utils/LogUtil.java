package com.api.automation.utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public final class LogUtil {

    private static final ThreadLocal<String> LOG_PATH = new ThreadLocal<>();
    private static final ThreadLocal<Scenario> SCENARIO = new ThreadLocal<>();
    private static final ThreadLocal<PrintStream> LOG_FILE = new ThreadLocal<>();

    private LogUtil() {}

    /**
     * Add logs to console/cucumber/logfile based on config properties.
     *
     * @param message message to be added to log
     */
    public static void log(String message) {
        log(message, "info");
    }

    /**
     * Add logs to console/cucumber/logfile based on config properties.
     *
     * @param stepName step name to be added to log
     */
    public static void logStep(String stepName) {
        log("%n%s%n---".formatted(stepName), "info");
    }

    /**
     * Add logs to console/cucumber/logfile based on config properties.
     *
     * @param message message to be added to log
     * @param status log/step status
     */
    public static void log(String message, String status) {
        addConsoleLog(message, status);
        addToLogFile(message, status);
        addCucumberLog(message);
        addAllureLog(message, status);
    }

    private static void addConsoleLog(String message, String status) {
        if (isConsoleLogEnabled()) {
            if (isErrorStatus(status)) {
                log.warn(message);
            } else {
                log.info(message);
            }
        }
    }

    private static void addToLogFile(String message, String status) {
        if (isLogFileEnabled() && getLogFile() != null) {
            getLogFile().println(StringUtils.isEmpty(status) ? message : "%s : %s".formatted(status.toUpperCase(), message));
        }
    }

    private static void addCucumberLog(String message) {
        if (isCucumberLogEnabled() && getScenario() != null) {
            getScenario().log(message);
        }
    }

    private static void addAllureLog(String message, String status) {
        if (isAllureLogEnabled()) {
            if (isErrorStatus(status)) {
                Allure.step(message, Status.FAILED);
            } else {
                Allure.step(message);
            }
        }
    }

    private static boolean isErrorStatus(String status) {
        return status.equalsIgnoreCase("error")
                || status.equalsIgnoreCase("failed")
                || status.equalsIgnoreCase("warning");
    }

    /**
     * Creates log file based on property "logFile".
     */
    public static PrintStream createLogFile() {
        PrintStream logFile;
        if (isLogFileEnabled()) {
            try {
                setLogPath();
                String logPath = getLogPath();
                if (Files.exists(Paths.get(logPath)) || new File(logPath).mkdirs()) {
                    String fileName = getLogFileName();
                    logFile = new PrintStream(new FileOutputStream(logPath + fileName));
                    setLogFile(logFile);
                    return logFile;
                }
            } catch (FileNotFoundException e) {
                log.warn("Error in creating log file");
            }
        }
        return null;
    }

    /**
     * Attaches log file to cucumber report based on property "attachLogs" or failed scenario.
     */
    public static void attachLogs() {
        if (Objects.nonNull(getScenario()) && (isAttachLogsEnabled() || getScenario().isFailed())) {
            String logName = "Execution Logs";
            try {
                Path logPath = Paths.get(getLogPath() + getLogFileName());
                getScenario().attach(Files.readAllBytes(logPath), "text/plain", logName);
            } catch (Exception exception) {
                log.error("Exception in attach logs.", exception);
            }
        }
    }

    public static Scenario getScenario() {
        return SCENARIO.get();
    }

    public static void setScenario(Scenario scenario) {
        LogUtil.SCENARIO.set(scenario);
    }

    public static PrintStream getLogFile() {
        return Objects.isNull(LOG_FILE.get()) ? createLogFile() : LOG_FILE.get();
    }

    public static void setLogFile(PrintStream logFile) {
        LogUtil.LOG_FILE.set(logFile);
    }

    public static boolean isConsoleLogEnabled() {
        return PropertiesUtil.getProperty("consoleLogs").equalsIgnoreCase("true");
    }

    private static boolean isCucumberLogEnabled() {
        return PropertiesUtil.getProperty("cucumberLogs").equalsIgnoreCase("true");
    }

    private static boolean isAllureLogEnabled() {
        return PropertiesUtil.getProperty("allureLogs").equalsIgnoreCase("true");
    }

    private static boolean isLogFileEnabled() {
        return PropertiesUtil.getProperty("logFile").equalsIgnoreCase("true");
    }

    private static boolean isAttachLogsEnabled() {
        return PropertiesUtil.getProperty("attachLogs").equalsIgnoreCase("true");
    }

    private static void setLogPath() {
        String basePath = "%s/reports/logs/".formatted(System.getProperty("user.dir"));
        String path = Objects.isNull(getScenario()) ? basePath :
                "%s%s/".formatted(basePath, getScenario().getUri().getSchemeSpecificPart().replace(".feature", "").replace("features/", ""));
        LOG_PATH.set(path);
    }

    public static String getLogPath() {
        return LOG_PATH.get();
    }

    private static String getLogFileName() {
        return Objects.isNull(getScenario()) ? "logs.txt" : getScenario().getName() + ".txt";
    }

    public static String getLogFilePath() {
        return getLogPath() + getLogFileName();
    }

    public static void clearLogData() {
        LOG_FILE.remove();
        SCENARIO.remove();
        LOG_PATH.remove();
    }
}
