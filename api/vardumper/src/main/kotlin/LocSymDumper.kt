import java.io.File
import java.io.FileWriter

fun main() {
    // Specify the IDs you want to lookup here
    val idsToLookup = listOf(317
    // Add more IDs as needed
    )

    // Path to the loc.sym file
//    val locSymFile = File("C:\\Users\\brand\\Documents\\RSPS\\rsmod\\.data\\symbols\\loc.sym")

    val fileType = "obj.sym"

    val objSymFile = File("C:\\Users\\artov\\IdeaProjects\\oldschool-realms\\.data\\symbols\\$fileType")

    val outputFile = File("loc_symbols_output.kt")

    if (!objSymFile.exists()) {
        println("Error: loc.sym file not found!")
        return
    }

    try {
        // Read and parse the loc.sym file
        val symbolMap = parseLocSymFile(objSymFile)

        // Generate output for the specified IDs
        generateOutput(symbolMap, idsToLookup, outputFile)

        println("Output written to ${outputFile.name}")
    } catch (e: Exception) {
        println("Error processing file: ${e.message}")
        e.printStackTrace()
    }
}

fun parseLocSymFile(file: File): Map<Int, String> {
    val symbolMap = mutableMapOf<Int, String>()

    file.forEachLine { line ->
        if (line.isNotBlank() && line.contains('\t')) {
            val parts = line.split('\t', limit = 2)
            if (parts.size == 2) {
                try {
                    val id = parts[0].toInt()
                    val symbolName = parts[1].trim()
                    symbolMap[id] = symbolName
                } catch (e: NumberFormatException) {
                    // Skip invalid lines
                }
            }
        }
    }

    return symbolMap
}

fun generateOutput(symbolMap: Map<Int, String>, idsToLookup: List<Int>, outputFile: File) {
    FileWriter(outputFile).use { writer ->
        writer.write("// Generated loc symbol references\n")
        writer.write("// Format: val sym_value_name = find(\"sym_value_name\")\n\n")

        for (id in idsToLookup) {
            val symbolName = symbolMap[id]
            if (symbolName != null) {
                writer.write("val $symbolName = find(\"$symbolName\")\n")
            } else {
                writer.write("// ID $id not found in loc.sym file\n")
            }
        }
    }

    // Also print to console for immediate viewing
    println("\nGenerated output:")
    for (id in idsToLookup) {
        val symbolName = symbolMap[id]
        if (symbolName != null) {
            println("val $symbolName = find(\"$symbolName\")")
        } else {
            println("// ID $id not found in loc.sym file")
        }
    }
}
