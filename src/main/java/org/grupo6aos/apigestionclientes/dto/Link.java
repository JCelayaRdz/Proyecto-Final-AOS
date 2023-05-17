package org.grupo6aos.apigestionclientes.dto;

public class Link {

    private String linkName;

    private String href;

    private String rel;

    public Link(String linkName, String href, String rel) {
        this.linkName = linkName;
        this.href = href;
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    @Override
    public String toString() {
        return linkName + " { = " +
                "href='" + href + '\'' +
                ", rel='" + rel + '\'' +
                '}';
    }
}
