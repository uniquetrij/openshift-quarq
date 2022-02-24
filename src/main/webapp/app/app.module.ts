import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { QuarqSharedModule } from 'app/shared/shared.module';
import { QuarqCoreModule } from 'app/core/core.module';
import { QuarqAppRoutingModule } from './app-routing.module';
import { QuarqHomeModule } from './home/home.module';
import { QuarqEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    QuarqSharedModule,
    QuarqCoreModule,
    QuarqHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    QuarqEntityModule,
    QuarqAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class QuarqAppModule {}
