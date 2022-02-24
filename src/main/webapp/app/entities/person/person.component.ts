import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerson } from 'app/shared/model/person.model';
import { PersonService } from './person.service';
import { PersonDeleteDialogComponent } from './person-delete-dialog.component';

@Component({
  selector: 'jhi-person',
  templateUrl: './person.component.html',
})
export class PersonComponent implements OnInit, OnDestroy {
  people?: IPerson[];
  eventSubscriber?: Subscription;

  constructor(protected personService: PersonService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.personService.query().subscribe((res: HttpResponse<IPerson[]>) => (this.people = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPeople();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPerson): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPeople(): void {
    this.eventSubscriber = this.eventManager.subscribe('personListModification', () => this.loadAll());
  }

  delete(person: IPerson): void {
    const modalRef = this.modalService.open(PersonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.person = person;
  }
}
