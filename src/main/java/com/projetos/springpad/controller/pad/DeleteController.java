/**
 * com.projetos.springpad.controller.pad.DeleteController
 * Apaga o registro identificado pelo `pad.id` (soft delete via status=DEL)
 */

package com.projetos.springpad.controller.pad;

import com.projetos.springpad.model.PadsModel;
import com.projetos.springpad.repository.PadsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class DeleteController {

    @Autowired
    private PadsRepository padsRepository;

    @GetMapping("/apaga/{id}")
    public String deletePad(
            @PathVariable Long id,
            @CookieValue(value = "owner_uid", required = false) String ownerUid,
            RedirectAttributes redirectAttributes,
            HttpSession session // força criação da sessão
    ) {
        Optional<PadsModel> optionalPad = padsRepository.findById(id);

        if (optionalPad.isEmpty() || optionalPad.get().getStatus() != PadsModel.Status.ON) {
            return "redirect:/";
        }

        PadsModel pad = optionalPad.get();

        boolean isOwner = false;
        if (ownerUid != null && !ownerUid.isEmpty() && pad.getOwnerModel() != null) {
            isOwner = ownerUid.equals(pad.getOwnerModel().getUid());
        }

        if (!isOwner) {
            return "redirect:/ver/" + id;
        }

        pad.setStatus(PadsModel.Status.DEL);
        padsRepository.save(pad);

        redirectAttributes.addFlashAttribute(
                "successMessage", pad.getTitle() + " excluído com sucesso!"
        );

        return "redirect:/";
    }

}