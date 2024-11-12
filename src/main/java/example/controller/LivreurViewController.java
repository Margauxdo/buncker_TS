package example.controller;

import example.entities.Livreur;
import example.services.LivreurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/livreurs")
public class LivreurViewController {

    @Autowired
    private LivreurService livreurService;

    @GetMapping
    public String listLivreurs(Model model) {
        List< Livreur> livreurs = livreurService.getAllLivreurs();
        model.addAttribute("livreurs", livreurs);
        return "livreurs/listLivreurs";
    }
    @GetMapping("/createLivreur")
    public String createLivreur(Model model) {
        model.addAttribute("livreur", new Livreur());
        return "livreurs/createLivreur";
    }
    @PostMapping("/createLivreur")
    public String createLivreur(@ModelAttribute ("livreur") Livreur livreur) {
        livreurService.createLivreur(livreur);
        return "redirect:/livreurs";
    }
    @GetMapping("/editLivreurs/{id}")
    public String editLivreur(@PathVariable("id") int id, Model model) {
        Livreur livreur = livreurService.getLivreurById(id);
        model.addAttribute("livreur", livreur);
        return "livreurs/editLivreur";
    }
    @PostMapping("/editLivreurs/{id}")
    public String editLivreur(@PathVariable("id")int id, @ModelAttribute Livreur livreur) {
        livreurService.updateLivreur(id, livreur);
        return "redirect:/livreurs";
    }
    @GetMapping("/deleteLivreur/{id}")
    public String deleteLivreur(@PathVariable("id") int id) {
        livreurService.deleteLivreur(id);
        return "redirect:/livreurs";
    }
}
