import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'person',
        loadChildren: () => import('./person/person.module').then(m => m.QuarqPersonModule),
      },
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.QuarqEmployeeModule),
      },
      {
        path: 'embedding',
        loadChildren: () => import('./embedding/embedding.module').then(m => m.QuarqEmbeddingModule),
      },
      {
        path: 'file',
        loadChildren: () => import('./file/file.module').then(m => m.QuarqFileModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class QuarqEntityModule {}
