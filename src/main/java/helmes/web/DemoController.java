package helmes.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import helmes.model.CustomerDto;
import helmes.model.SectorRowDto;
import helmes.service.CustomerService;
import helmes.service.SectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@ConditionalOnProperty(name = "sections.one_query", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class DemoController {
    private static final String SECTORS_LIST_BEAN_NAME = "sectorsList";
    private static final String MODEL_BEAN_NAME = "customerDto";
    private static final String VIEW_NAME = "customer";

    private final SectorService sectorService;
    private final CustomerService customerService;

    @GetMapping("/")
    public String customerForm(Model model) {
        CustomerDto dto = new CustomerDto();
        model.addAttribute(MODEL_BEAN_NAME, dto);
        model.addAttribute(SECTORS_LIST_BEAN_NAME, loadAllSectors());
        return VIEW_NAME;
    }

    @PostMapping("/")
    public String customerSubmit(@ModelAttribute @Valid CustomerDto customerDto, BindingResult bindingResult, Model model) {
        model.addAttribute(SECTORS_LIST_BEAN_NAME, loadAllSectors());
        if (!bindingResult.hasErrors()) {
            customerDto = customerService.save(customerDto);
            model.addAttribute(MODEL_BEAN_NAME, customerDto);
        }
        return VIEW_NAME;
    }

    private List<SectorRowDto> loadAllSectors() {
        return Utils.tranformToPlain(sectorService.loadAllInOneQuery());
    }

}
