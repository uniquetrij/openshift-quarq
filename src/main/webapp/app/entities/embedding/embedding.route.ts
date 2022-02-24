import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmbedding, Embedding } from 'app/shared/model/embedding.model';
import { EmbeddingService } from './embedding.service';
import { EmbeddingComponent } from './embedding.component';
import { EmbeddingDetailComponent } from './embedding-detail.component';
import { EmbeddingUpdateComponent } from './embedding-update.component';

@Injectable({ providedIn: 'root' })
export class EmbeddingResolve implements Resolve<IEmbedding> {
  constructor(private service: EmbeddingService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmbedding> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((embedding: HttpResponse<Embedding>) => {
          if (embedding.body) {
            return of(embedding.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Embedding());
  }
}

export const embeddingRoute: Routes = [
  {
    path: '',
    component: EmbeddingComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Embeddings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmbeddingDetailComponent,
    resolve: {
      embedding: EmbeddingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Embeddings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmbeddingUpdateComponent,
    resolve: {
      embedding: EmbeddingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Embeddings',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmbeddingUpdateComponent,
    resolve: {
      embedding: EmbeddingResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Embeddings',
    },
    canActivate: [UserRouteAccessService],
  },
];
