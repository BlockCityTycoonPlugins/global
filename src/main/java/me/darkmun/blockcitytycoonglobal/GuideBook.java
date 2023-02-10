package me.darkmun.blockcitytycoonglobal;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import static me.darkmun.blockcitytycoonglobal.storages.Configs.bookConfig;

public class GuideBook extends ItemStack {

    public GuideBook() {
        super(Material.WRITTEN_BOOK);
        BookMeta book = (BookMeta) this.getItemMeta();
        book.setTitle(bookConfig.getConfig().getString("title"));
        book.setAuthor(bookConfig.getConfig().getString("author"));
        book.setPages(bookConfig.getConfig().getStringList("pages"));

        this.setItemMeta(book);
    }
}
