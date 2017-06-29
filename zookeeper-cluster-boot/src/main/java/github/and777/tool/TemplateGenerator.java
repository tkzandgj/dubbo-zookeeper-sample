package github.and777.tool;

import java.io.IOException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
 * @author edliao on 2017/6/23.
 * @description 模块模板生成器
 */
@Slf4j
public class TemplateGenerator {

  private static final String DEFAULT_TEMPLATE = "/zoo-template.cfg";
  GroupTemplate groupTemplate;

  public TemplateGenerator() {
    try {
      ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("");
      Configuration cfg = Configuration.defaultConfiguration();
      groupTemplate = new GroupTemplate(resourceLoader, cfg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 返回生成的内容
   */
  public String generate(Consumer<Template> bindConsumer) {
    Template template = groupTemplate.getTemplate(DEFAULT_TEMPLATE);
    bindConsumer.accept(template);
    return template.render();
  }
}
