package me.darkmun.blockcitytycoonglobal;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static me.darkmun.blockcitytycoonglobal.storages.Configs.bookConfig;

public class GuideBook extends ItemStack {

    public GuideBook() {
        super(Material.WRITTEN_BOOK);
        BookMeta book = (BookMeta) this.getItemMeta();
        book.setTitle(bookConfig.getString("title"));
        book.setAuthor(bookConfig.getString("author"));
        book.setPages(bookConfig.getStringList("pages"));

        this.setItemMeta(book);
    }
}
