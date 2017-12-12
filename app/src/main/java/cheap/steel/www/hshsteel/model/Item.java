package cheap.steel.www.hshsteel.model;

public class Item {
	private String itemcode, thumbnailUrl, description, quantityuom, quantityuom1, quantityuom2, quantityuom3, unitprice, unitprice1, unitprice2, unitprice3, gst, gst1, gst2, gst3;

	public Item() {
	}

	public Item(String itemcode, String thumbnailUrl, String description, String quantityuom, String unitprice, String quantityuom1, String unitprice1, String quantityuom2, String unitprice2, String quantityuom3, String unitprice3, String gst, String gst1, String gst2, String gst3) {
		this.itemcode = itemcode;
		this.thumbnailUrl = thumbnailUrl;
		this.description = description;
		this.quantityuom = quantityuom;
		this.quantityuom1 = quantityuom1;
		this.quantityuom2 = quantityuom2;
		this.quantityuom3 = quantityuom3;
		this.unitprice = unitprice;
        this.unitprice1 = unitprice1;
        this.unitprice2 = unitprice2;
        this.unitprice3 = unitprice3;
        this.gst = gst;
        this.gst1 = gst1;
        this.gst2 = gst2;
        this.gst3 = gst3;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantityuom() {
		return quantityuom;
	}

	public void setQuantityuom(String quantityuom) {
		this.quantityuom = quantityuom;
	}

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getGst() { return gst; }

    public void setGst(String gst) { this.gst = gst; }

    public String getQuantityuom1() {
        return quantityuom1;
    }

    public void setQuantityuom1(String quantityuom1) {
        this.quantityuom1 = quantityuom1;
    }

    public String getUnitprice1() {
        return unitprice1;
    }

    public void setUnitprice1(String unitprice1) {
        this.unitprice1 = unitprice1;
    }

    public String getGst1() { return gst1; }

    public void setGst1(String gst1) { this.gst1 = gst1; }

    public String getQuantityuom2() {
        return quantityuom2;
    }

    public void setQuantityuom2(String quantityuom2) {
        this.quantityuom2 = quantityuom2;
    }

    public String getUnitprice2() {
        return unitprice2;
    }

    public void setUnitprice2(String unitprice2) {
        this.unitprice2 = unitprice2;
    }

    public String getGst2() { return gst2; }

    public void setGst2(String gst2) { this.gst2 = gst2; }

    public String getQuantityuom3() {
        return quantityuom3;
    }

    public void setQuantityuom3(String quantityuom3) {
        this.quantityuom3 = quantityuom3;
    }

    public String getUnitprice3() {
        return unitprice3;
    }

    public void setUnitprice3(String unitprice3) {
        this.unitprice3 = unitprice3;
    }

    public String getGst3() { return gst3; }

    public void setGst3(String gst3) { this.gst3 = gst3; }

}
