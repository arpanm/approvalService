import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprovalRequestItem, getApprovalRequestItemIdentifier } from '../approval-request-item.model';

export type EntityResponseType = HttpResponse<IApprovalRequestItem>;
export type EntityArrayResponseType = HttpResponse<IApprovalRequestItem[]>;

@Injectable({ providedIn: 'root' })
export class ApprovalRequestItemService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/approval-request-items');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(approvalRequestItem: IApprovalRequestItem): Observable<EntityResponseType> {
    return this.http.post<IApprovalRequestItem>(this.resourceUrl, approvalRequestItem, { observe: 'response' });
  }

  update(approvalRequestItem: IApprovalRequestItem): Observable<EntityResponseType> {
    return this.http.put<IApprovalRequestItem>(
      `${this.resourceUrl}/${getApprovalRequestItemIdentifier(approvalRequestItem) as number}`,
      approvalRequestItem,
      { observe: 'response' }
    );
  }

  partialUpdate(approvalRequestItem: IApprovalRequestItem): Observable<EntityResponseType> {
    return this.http.patch<IApprovalRequestItem>(
      `${this.resourceUrl}/${getApprovalRequestItemIdentifier(approvalRequestItem) as number}`,
      approvalRequestItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApprovalRequestItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApprovalRequestItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprovalRequestItemToCollectionIfMissing(
    approvalRequestItemCollection: IApprovalRequestItem[],
    ...approvalRequestItemsToCheck: (IApprovalRequestItem | null | undefined)[]
  ): IApprovalRequestItem[] {
    const approvalRequestItems: IApprovalRequestItem[] = approvalRequestItemsToCheck.filter(isPresent);
    if (approvalRequestItems.length > 0) {
      const approvalRequestItemCollectionIdentifiers = approvalRequestItemCollection.map(
        approvalRequestItemItem => getApprovalRequestItemIdentifier(approvalRequestItemItem)!
      );
      const approvalRequestItemsToAdd = approvalRequestItems.filter(approvalRequestItemItem => {
        const approvalRequestItemIdentifier = getApprovalRequestItemIdentifier(approvalRequestItemItem);
        if (approvalRequestItemIdentifier == null || approvalRequestItemCollectionIdentifiers.includes(approvalRequestItemIdentifier)) {
          return false;
        }
        approvalRequestItemCollectionIdentifiers.push(approvalRequestItemIdentifier);
        return true;
      });
      return [...approvalRequestItemsToAdd, ...approvalRequestItemCollection];
    }
    return approvalRequestItemCollection;
  }
}
