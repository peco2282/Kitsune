package com.peco2282.kitsune.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class IoTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun testFileChain() {
        val testFile = File(tempDir, "test.txt")
        val chain = file(testFile)

        // write & read
        chain write "hello"
        assertEquals("hello", chain.read())

        // append
        chain append " world"
        assertEquals("hello world", chain.read())

        // exists & name
        assertTrue(chain.exists)
        assertEquals("test.txt", chain.name)

        // rename
        val renamedChain = chain rename "renamed.txt"
        assertFalse(testFile.exists())
        assertTrue(renamedChain.exists)
        assertEquals("renamed.txt", renamedChain.name)
        assertEquals("hello world", renamedChain.read())
    }

    @Test
    fun testDirectoryOperations() {
        val dir = file(File(tempDir, "subdir"))
        assertFalse(dir.exists)

        dir.ensureExists()
        assertTrue(dir.exists)
        assertTrue(dir.isDirectory)

        val childFile = dir.child("child.txt")
        childFile write "child content"
        assertTrue(childFile.exists)

        val files = dir.listFiles()
        assertEquals(1, files.size)
        assertEquals("child.txt", files[0].name)

        dir.delete()
        assertFalse(dir.exists)
    }
}
