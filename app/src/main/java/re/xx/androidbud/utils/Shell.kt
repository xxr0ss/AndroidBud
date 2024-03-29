package re.xx.androidbud.utils

import android.util.Log
import java.io.*

object Shell {
    private val TAG: String = "Shell"
    var rooted = false

    fun tryRoot(pkgCodePath: String) {
        // try exec su and refresh `rooted`
        var process: Process? = null
        var dos: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            dos = DataOutputStream(process.outputStream)
            dos.writeBytes("chmod 777 ${pkgCodePath}\n")
            dos.writeBytes("exit\n")
            dos.flush()
            rooted = process.waitFor() == 0
        } catch (e: Exception) {
            rooted = false
        } finally {
            dos?.close()
            process?.destroy()
        }
    }

    // TODO 避免读取输出时的阻塞
    fun execCmd(cmd: String, readOutput: Boolean=true): String {
        var out=""
        val process = Runtime.getRuntime().exec(cmd)
        if (readOutput) {
            var br = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while ((br.readLine().also { line = it }) != null) {
                out += line + "\n"
            }
            br.close()
            out += "\n"
            br = BufferedReader(InputStreamReader(process.errorStream))
            while ((br.readLine().also { line = it }) != null) {
                out += line + "\n"
            }
            br.close()
        }

        return out;
    }

    fun execRootCmd(cmd: String, readOutput: Boolean = true): String {
        if (!rooted) return ""
        var out = ""
        try {
            val process = Runtime.getRuntime().exec("su")
            val stdin = DataOutputStream(process.outputStream)
            val stdout = process.inputStream
            val stderr = process.errorStream

            Log.i(TAG, "execRootCmd: $cmd")
            stdin.writeBytes(cmd + "\n")
            stdin.flush()
            stdin.writeBytes("exit\n")
            stdin.flush()
            stdin.close()

            if (readOutput) {
                var br = BufferedReader(InputStreamReader(stdout))
                var line: String?

                while ((br.readLine().also { line = it }) != null) {
                    out += line + "\n"
                }
                br.close()
                out += "\n"
                br = BufferedReader(InputStreamReader(stderr))
                while ((br.readLine().also { line = it }) != null) {
                    out += line + "\n"
                }
                br.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
        return out
    }
}