package cheap.steel.www.hshsteel.model;

public class Item {
	private String itemcode, thumbnailUrl, description, quantityuom, unitprice;

	public Item() {
	}

	public Item(String itemcode, String thumbnailUrl, String description, String quantityuom, String unitprice) {
		this.itemcode = itemcode;
		this.thumbnailUrl = thumbnailUrl;
		this.description = description;
		this.quantityuom = quantityuom;
		this.unitprice = unitprice;
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

}
