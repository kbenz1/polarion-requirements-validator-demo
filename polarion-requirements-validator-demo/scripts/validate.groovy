#!/usr/bin/env groovy

/*
 * Small script-based validation example.
 * This is not presented as a Polarion production script.
 */

def id = args ? args[0] : "REQ-1001"

if (!(id ==~ /REQ-\d{4,}/)) {
    System.err.println("Invalid requirement id: ${id}")
    System.exit(1)
}

println "Requirement id ${id} is syntactically valid."
