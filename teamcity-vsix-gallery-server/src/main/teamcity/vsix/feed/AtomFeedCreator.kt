package teamcity.vsix.feed

import com.intellij.openapi.diagnostic.Logger
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import teamcity.vsix.index.PackagesIndex
import teamcity.vsix.index.VsixPackage
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import java.util.ArrayList
import com.mycila.xmltool.XMLDoc
import jetbrains.buildServer.util.Util

class AtomFeedCreator(val index: PackagesIndex) {
    val LOG = Logger.getInstance("teamcity.vsix");
    val gallery_id = "uuid:38ba4411-63e2-425c-8798-7dace9b99c39;id=1"

    fun handleRequest(request: HttpServletRequest, response: HttpServletResponse) {
        val entries = index.getPackageEntries()

        LOG.info("Got entries: " + entries)

        val xml = createFeed2(entries);
        response.setContentType("application/xml");
        response.getOutputStream().write(xml.toByteArray())
    }

    fun createFeed2(packages: Collection<VsixPackage>): String {
        val current = Thread.currentThread().getContextClassLoader()
        Thread.currentThread().setContextClassLoader(javaClass<XMLDoc>().getClassLoader())
        try {
            val tag = XMLDoc.newDocument(false)
                    .addDefaultNamespace("http://www.w3.org/2005/Atom")
                    .addRoot("feed")
                    .addTag("title").addText("VSIX Gallery")
                    .gotoRoot().addTag("id").setText(gallery_id)
                    .gotoRoot().addTag("updated").addText(DateTime.now(DateTimeZone.UTC).toString())

            packages.forEach {
                val entry = tag.addTag("entry")
                entry.addTag("id").addText(it.Id)
                entry.addTag("title").addAttribute("type", "text").addText(it.Title)
                entry.addTag("summary").addAttribute("type", "text").addText(it.Summary)
                entry.addTag("published").addText(DateTime.now(DateTimeZone.UTC).toString())
                entry.addTag("updated").addText(DateTime.now(DateTimeZone.UTC).toString())
                entry.addTag("author").addTag("name").addText(it.AuthorName)
                entry.gotoRoot().addTag("content").addAttribute("type", "application/octet-stream").addAttribute("src", it.ContentPath)
                val vsix = entry.gotoRoot().addTag("Vsix")
                vsix.addTag("Id").addText(it.Id)
                vsix.addTag("Version").addText(it.Version)
                vsix.addTag("References")


            }

            return tag.toString()
        } finally {
            Thread.currentThread().setContextClassLoader(current)
        }

    }

    fun createFeed(requestURL: String, packages: Collection<VsixPackage>) : XML {
        return xml {
            feed {
                title { +"VSIX Gallery" }
                id { +gallery_id }
                updated { +DateTime.now().toString() }
            }
            packages.forEach {
                entry {
                    id { +it.Id }
                    title { +it.Title }
                    summary { +it.Summary }

                    // published
                    // updated

                    author {
                        name { +it.AuthorName }
                    }

                    link(rel = "icon", href = "") { }
                    link(rel = "previewimage", href = "") { }

                    content(type = "application/octet-stream", src = it.ContentPath) { }

                    Vsix {
                        Id { +it.Id }
                        Version { +it.Version }
                        References { }
                    }
                }
            }
        }
    }

    trait Element {
        fun render(builder: StringBuilder, indent: String)

        override fun toString(): String {
            val builder = StringBuilder()
            render(builder, "")
            return builder.toString()
        }
    }

    class TextElement(val text: String) : Element {
        override fun render(builder: StringBuilder, indent: String) {
            builder.append("$indent$text\n")
        }
    }

    abstract class Tag(val name: String) : Element {
        val children = arrayListOf<Element>()
        val attributes = hashMapOf<String, String>()

        protected fun initTag<T : Element>(tag: T, init: T.() -> Unit): T {
            tag.init()
            children.add(tag)
            return tag
        }

        override fun render(builder: StringBuilder, indent: String) {
            builder.append("$indent<$name${renderAttributes()}>\n")
            for (c in children) {
                c.render(builder, indent)
            }
            builder.append("$indent</$name>\n")
        }

        private fun renderAttributes(): String? {
            val builder = StringBuilder()
            for (a in attributes.keySet()) {
                builder.append(" $a=\"${attributes[a]}\"")
            }
            return builder.toString()
        }
    }

    abstract class TagWithText(name: String) : Tag(name) {
        fun String.plus() {
            children.add(TextElement(this))
        }
    }

    class XML() : TagWithText("xml") {
        fun feed(init: Feed.() -> Unit) = initTag(Feed(), init)

        fun entry(init: Entry.() -> Unit) = initTag(Entry(), init)
    }

    class Feed() : TagWithText("feed") {
        {
            attributes["xmlns"] = "http://www.w3.org/2005/Atom"
        }

        fun title(type: String = "text", init: Title.() -> Unit) {
            val title = initTag(Title(), init)
            title.type = type
        }

        fun id(init: Id.() -> Unit) = initTag(Id(), init)

        fun updated(init: Updated.() -> Unit) = initTag(Updated(), init)
    }

    class Title() : TextTag("title")
    class Summary() : TextTag("summary")

    abstract class TextTag(text: String) : TagWithText(text) {
        public var type: String
            get() = attributes["type"]
            set(value) {
                attributes["type"] = value
            }
    }

    class Author : TagWithText("author") {
        fun name(init: Name.() -> Unit) = initTag(Name(), init)
    }

    class Content : TagWithText("content") {
        public var type: String
            get() = attributes["type"]
            set(value) {
                attributes["type"] = value
            }
        public var src: String
            get() = attributes["src"]
            set(value) {
                attributes["src"] = value
            }

    }

    class Name : TagWithText("name")

    class Vsix() : TagWithText("Vsix") {
        {
            attributes["xmlns"] = "http://schemas.microsoft.com/developer/vsx-syndication-schema/2010"
            attributes["xmlns:xsd"] = "http://www.w3.org/2001/XMLSchema"
            attributes["xmlns:xsi"] = "http://www.w3.org/2001/XMLSchema-instance"
        }

        fun Id(init: Id.() -> Unit) = initTag(Id(), init)

        fun Version(init: Version.() -> Unit) = initTag(Version(), init)

        fun References(init: References.() -> Unit) = initTag(References(), init)
    }

    class Version() : TagWithText("Version")
    class References() : TagWithText("References")

    class Entry() : TagWithText("entry") {
        fun id(init: Id.() -> Unit) = initTag(Id(), init)

        fun title(init: Title.() -> Unit) {
            val title = initTag(Title(), init)
                title.type = "text"
        }

        fun summary(init: Summary.() -> Unit) {
            val title = initTag(Summary(), init)
            title.type = "text"
        }

        fun author(init: Author.() -> Unit) = initTag(Author(), init)

        fun link(rel: String, href: String, init: Link.() -> Unit) {
            val link = initTag(Link(), init)
            link.rel = rel
            link.href = href
        }

        fun content(type: String = "application/octet-stream", src: String, init: Content.() -> Unit) {
            val content = initTag(Content(), init)
            content.type = type
            content.src = src
        }

        fun Vsix(init: Vsix.() -> Unit) = initTag(Vsix(), init)
    }

    class Id() : TagWithText("id")
    class Updated() : TagWithText("updated")

    class Link() : TagWithText("link") {
        public var rel: String
            get() = attributes["rel"]
            set(value) {
                attributes["rel"] = value
            }
        public var href: String
            get() = attributes["href"]
            set(value) {
                attributes["href"] = value
            }
    }

    fun xml(init: XML.() -> Unit): XML {
        val xml = XML()
        xml.init()
        return xml
    }
}