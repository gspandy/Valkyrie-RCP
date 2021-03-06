package org.valkyriercp.sample.simple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.AbstractApplicationConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.DefaultViewDescriptor;
import org.valkyriercp.application.support.SingleViewPageDescriptor;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.sample.simple.domain.ContactDataStore;
import org.valkyriercp.sample.simple.domain.SimpleValidationRulesSource;
import org.valkyriercp.sample.simple.ui.ContactView;
import org.valkyriercp.sample.simple.ui.InitialView;

import java.util.List;
import java.util.Map;

@Configuration
public class SimpleSampleApplicationConfig extends AbstractApplicationConfig {

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor lifecycleAdvisor =  super.applicationLifecycleAdvisor();
        lifecycleAdvisor.setStartingPageDescriptor(new SingleViewPageDescriptor(initialView()));
        return lifecycleAdvisor;
    }

    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        initializer.setPreStartupCommands(Lists.newArrayList("loginCommand"));
        return initializer;
    }

    @Override
    public List<String> getResourceBundleLocations() {
        List<String> list = super.getResourceBundleLocations();
        list.add("org.valkyriercp.sample.simple.simple");
        return list;
    }

    public Map<String, Resource> getImageSourceResources() {
        Map<String, Resource> resources = super.getImageSourceResources();
        resources.put("simple", applicationContext().getResource("classpath:/org/valkyriercp/sample/simple/images.properties"));
        return resources;
    }

    @Override
    public Class<?> getCommandConfigClass() {
        return SimpleSampleCommandConfig.class;
    }

    @Override
    public RulesSource rulesSource() {
        return new SimpleValidationRulesSource();
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

    @Bean
    public ViewDescriptor initialView() {
        InitialView view = new InitialView();
        view.setFirstMessage("firstMessage.text");
        view.setDescriptionTextPath(new ClassPathResource("/org/valkyriercp/sample/simple/ui/initialViewText.html"));
        return view.getDescriptor();
    }

    @Bean
    public ViewDescriptor contactView() {
        DefaultViewDescriptor initialView = new DefaultViewDescriptor("contactView", ContactView.class);
        Map<String,Object> viewProperties = Maps.newHashMap();
        viewProperties.put("contactDataStore", new ContactDataStore());
        initialView.setViewProperties(viewProperties);
        return initialView;
    }
}
