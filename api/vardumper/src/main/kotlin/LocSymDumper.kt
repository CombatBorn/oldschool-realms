import java.io.File
import java.io.FileWriter

fun main() {
    // Specify the IDs you want to lookup here
    val idsToLookup = listOf(
        11363,
        11362,
        34773,
        11382,
        11383,
        11384,
        11385,
        41547,
        41548,
        41549,
        41550,
        17962,
        17963,
        17964,
        17965,
        30985,
        30986,
        11367,
        11366,
        36204,
        26661,
        26662,
        26663,
        26664,
        6971,
        6973,
        6975,
        6977,
        8975,
        8977,
        8979,
        8976,
        8978,
        8980,
        11381,
        11380,
        9030,
        9031,
        9032,
        11371,
        11370,
        36206,
        51485,
        51487,
        51489,
        51491,
        28496,
        28497,
        28498,
        11387,
        11373,
        11372,
        36207,
        15250,
        15251,
        39095,
        39094,
        13356,
        11375,
        11374,
        36208,
        36210,
        22593,
        22595,
        22597,
        46701,
        46702,
        56362,
        56363,
        56359,
        56360,
        11377,
        11376,
        36209,
        11388,
        11389,
        11393
        // Add more IDs as needed
    )

    // Path to the loc.sym file
//    val locSymFile = File("C:\\Users\\brand\\Documents\\RSPS\\rsmod\\.data\\symbols\\loc.sym")

    val fileType = "loc.sym"

    val objSymFile = File("C:\\Users\\brand\\Documents\\RSPS\\rsmod\\.data\\symbols\\$fileType")

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
