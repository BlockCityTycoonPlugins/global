package me.darkmun.blockcitytycoonglobal;

import me.darkmun.blockcitytycoonglobal.storages.Configs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

import static me.darkmun.blockcitytycoonglobal.storages.Configs.bookConfig;

public class GuideBook extends ItemStack {

    public GuideBook() {
        super(Material.WRITTEN_BOOK);
        BookMeta book = (BookMeta) this.getItemMeta();
        book.setTitle(bookConfig.getString("title"));
        book.setAuthor(bookConfig.getString("author"));
        book.setPages(bookConfig.getStringList("pages"));

        this.setItemMeta(book);
        /*if (bookConfig.getBoolean("lore.enable")) {
            List<String> authors = bookConfig.getStringList("lore.authors");
            Bukkit.getLogger().info(String.valueOf(bookConfig.getBoolean("lore.enable")));
            Bukkit.getLogger().info(authors.get(0));
            if (authors.size() == 1) {
                book.setLore(Collections.singletonList("Автор: " + authors.get(0)));
            } else {
                StringBuilder authorsString = new StringBuilder();
                for (String author : authors) {
                    if (authors.lastIndexOf(author) == authors.size() - 1) {
                        authorsString.append(author);
                    }
                    authorsString.append(author).append(", ");
                }
                book.setLore(Collections.singletonList("Авторы: " + authorsString));
            }
        } else {
            book.setLore(null);
        }*/
    }
}
